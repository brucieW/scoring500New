import com.zeroboss.scoring500.data.repository.ScoringRepositoryImpl
import com.zeroboss.scoring500.domain.repository.ScoringRepository
import io.objectbox.BoxStore
import org.koin.dsl.module

val scoringRepositoryModule = module {
    single { provideScoringRepository(get()) }
}

private fun provideScoringRepository(
    boxStore: BoxStore
) : ScoringRepository = ScoringRepositoryImpl(boxStore)

