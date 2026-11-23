package io.aparok.app.domain.objects.options

import io.aparok.app.core_models.DVFilter
import io.aparok.app.core_models.DVFilterCondition
import io.aparok.app.core_models.Relations
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.test_utils.MockDataFactory
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GetOptionsTest {

    @Mock
    lateinit var repo: BlockRepository

    private lateinit var useCase: GetOptions

    private val spaceId = MockDataFactory.randomUuid()
    private val relationKey = MockDataFactory.randomString()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = GetOptions(repo = repo)
    }

    @Test
    fun `should use NOT_EQUAL true for IS_DELETED so options with null field are included`() {
        runBlocking {
            useCase.run(GetOptions.Params(space = spaceId, relation = relationKey))
        }

        val filters = captureFilters()
        val isDeletedFilter = filters.first { it.relation == Relations.IS_DELETED }
        assertEquals(DVFilterCondition.NOT_EQUAL, isDeletedFilter.condition)
        assertEquals(true, isDeletedFilter.value)
    }

    @Test
    fun `should use NOT_EQUAL true for IS_ARCHIVED so options with null field are included`() {
        runBlocking {
            useCase.run(GetOptions.Params(space = spaceId, relation = relationKey))
        }

        val filters = captureFilters()
        val isArchivedFilter = filters.first { it.relation == Relations.IS_ARCHIVED }
        assertEquals(DVFilterCondition.NOT_EQUAL, isArchivedFilter.condition)
        assertEquals(true, isArchivedFilter.value)
    }

    @Test
    fun `should not use EQUAL false for IS_DELETED which would exclude options with null field`() {
        runBlocking {
            useCase.run(GetOptions.Params(space = spaceId, relation = relationKey))
        }

        val filters = captureFilters()
        val isDeletedFilter = filters.first { it.relation == Relations.IS_DELETED }
        assertFalse(
            "EQUAL false excludes options with null IS_DELETED — must use NOT_EQUAL true",
            isDeletedFilter.condition == DVFilterCondition.EQUAL && isDeletedFilter.value == false
        )
    }

    // Read invocations directly to bypass Mockito's matcher infrastructure —
    // it fails on value class params (SpaceId) with NPE on unbox-impl.
    @Suppress("UNCHECKED_CAST")
    private fun captureFilters(): List<DVFilter> {
        val invocations = Mockito.mockingDetails(repo).invocations
        // The method name is mangled (e.g. "searchObjects-_6PZyZ4") because SpaceId is a value class.
        val call = invocations.first { it.method.name.startsWith("searchObjects") }
        // searchObjects(space, sorts, filters, fulltext, offset, limit, keys) — filters is index 2
        return call.arguments[2] as List<DVFilter>
    }
}
