package ru.prsolution.winstrike.presentation

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import ru.prsolution.winstrike.BuildConfig
import ru.prsolution.winstrike.data.datasource.*
import ru.prsolution.winstrike.data.repository.AppRepositoryImpl
import ru.prsolution.winstrike.data.repository.ArenaRepositoryImpl
import ru.prsolution.winstrike.data.repository.CityRepositoryImpl
import ru.prsolution.winstrike.data.repository.LoginRepositoryImpl
import ru.prsolution.winstrike.datasource.cache.ArenaCacheDataSourceImpl
import ru.prsolution.winstrike.datasource.cache.CityCacheDataSourceImpl
import ru.prsolution.winstrike.datasource.cache.LoginCacheDataSourceImpl
import ru.prsolution.winstrike.datasource.remote.*
import ru.prsolution.winstrike.domain.models.arena.Arena
import ru.prsolution.winstrike.domain.models.city.City
import ru.prsolution.winstrike.domain.models.login.UserModel
import ru.prsolution.winstrike.domain.repository.AppRepository
import ru.prsolution.winstrike.domain.repository.ArenaRepository
import ru.prsolution.winstrike.domain.repository.CityRepository
import ru.prsolution.winstrike.domain.repository.LoginRepository
import ru.prsolution.winstrike.domain.usecases.*
import ru.prsolution.winstrike.networking.*
import ru.prsolution.winstrike.presentation.utils.cache.Cache
import ru.prsolution.winstrike.viewmodel.*

fun injectFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(
        viewModelModule,
        useCaseModule,
        repositoryModule,
        dataSourceModule,
        networkModule,
        cacheModule
    )
}

val viewModelModule: Module = module {
    viewModel { AppViewModel(appUseCase = get()) }
    viewModel { CityListViewModel(cityUseCase = get()) }
    viewModel { CityItemViewModel(arenaUseCase = get()) }
    viewModel { SetUpViewModel(arenaUseCase = get()) }
    viewModel { MapViewModel(paymentUseCase = get()) }
    viewModel { LoginViewModel(loginUseCase = get()) }
    viewModel { RegisterViewModel(loginUseCase = get()) }
    viewModel { SmsViewModel(loginUseCase = get()) }
    viewModel { ProfileViewModel(loginUseCase = get()) }
    viewModel { OrderViewModel(arenaUseCase = get()) }
    viewModel { FCMViewModel(arenaUseCase = get()) }
}

val useCaseModule: Module = module {
    factory { AppUseCase(appRepository = get()) }
    factory { CityUseCase(cityRepository = get()) }
    factory { ArenaUseCase(arenaRepository = get()) }
    factory { PaymentUseCase(arenaRepository = get()) }
    factory { LoginUseCase(loginRepository = get()) }
}

val repositoryModule: Module = module {
    single { AppRepositoryImpl( remoteDataSource = get()) as AppRepository }
    single { CityRepositoryImpl(cacheDataSource = get(), remoteDataSource = get()) as CityRepository }
    single { ArenaRepositoryImpl(cacheDataSource = get(), remoteDataSource = get()) as ArenaRepository }
    single { LoginRepositoryImpl(cacheDataSource = get(), remoteDataSource = get()) as LoginRepository }
}

val dataSourceModule: Module = module {
    single { AppRemoteDataSourceImpl(api = appApi) as AppRemoteDataSource }

    single { CityCacheDataSourceImpl(cache = get(CITY_CACHE)) as CityCacheDataSource }
    single { CityRemoteDataSourceImpl(api = cityApi) as CityRemoteDataSource }

    single { ArenaCacheDataSourceImpl(cache = get(ARENA_CACHE)) as ArenaCacheDataSource }
    single { ArenaRemoteDataSourceImpl(api = arenaApi) as ArenaRemoteDataSource }

    single { LoginCacheDataSourceImpl(cache = get(LOGIN_CACHE)) as LoginCacheDataSource }
    single { LoginRemoteDataSourceImpl(api = userApi) as LoginRemoteDataSource }
}

val networkModule: Module = module {
    single { appApi }
    single { cityApi }
    single { arenaApi }
    single { userApi }
}

val cacheModule: Module = module {
    single(name = CITY_CACHE) { Cache<List<City>>() }
    single(name = ARENA_CACHE) { Cache<List<Arena>>() }
    single(name = LOGIN_CACHE) { Cache<UserModel>() }
}

//private const val DEV_URL = BuildConfig.DEV_URL
//private const val DEV_URL = BuildConfig.DEV_URL
private const val PROD_URL = BuildConfig.PROD_URL


private val retrofit: Retrofit = createNetworkClient(PROD_URL, BuildConfig.DEBUG)
//private val retrofit: Retrofit = createNetworkClient(DEV_URL, BuildConfig.DEBUG)

private val appApi: AppApi = retrofit.create(AppApi::class.java)
private val cityApi: CityApi = retrofit.create(CityApi::class.java)
private val arenaApi: ArenaApi = retrofit.create(ArenaApi::class.java)
private val userApi: UserApi = retrofit.create(UserApi::class.java)

private const val CITY_CACHE = "CITY_CACHE"
private const val ARENA_CACHE = "ARENA_CACHE"
private const val LOGIN_CACHE = "LOGIN_CACHE"
