package com.onrushers.data.internal.di.modules;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onrushers.data.api.interceptor.InvalidTokenInterceptor;
import com.onrushers.data.api.service.AuthenticationService;
import com.onrushers.data.api.service.BoostService;
import com.onrushers.data.api.service.CommentService;
import com.onrushers.data.api.service.EventService;
import com.onrushers.data.api.service.ExploreService;
import com.onrushers.data.api.service.FeedService;
import com.onrushers.data.api.service.FileService;
import com.onrushers.data.api.service.GoogleService;
import com.onrushers.data.api.service.OtherService;
import com.onrushers.data.api.service.RelationService;
import com.onrushers.data.api.service.ReportService;
import com.onrushers.data.api.service.ResetPasswordService;
import com.onrushers.data.api.service.SearchService;
import com.onrushers.data.api.service.UserService;
import com.onrushers.data.repository.auth_session.AuthSessionPrefsRepositoryImpl;
import com.onrushers.data.repository.authentication.AuthenticationRestRepositoryImpl;
import com.onrushers.data.repository.boost.BoostApiRepositoryImpl;
import com.onrushers.data.repository.comment.CommentApiRepositoryImpl;
import com.onrushers.data.repository.event.EventApiRepositoryImpl;
import com.onrushers.data.repository.explore.ExploreApiRepositoryImpl;
import com.onrushers.data.repository.feed.FeedApiRepositoryImpl;
import com.onrushers.data.repository.file.FileApiRepositoryImpl;
import com.onrushers.data.repository.other.OtherApiRepositoryImpl;
import com.onrushers.data.repository.place.PlaceApiRepositoryImpl;
import com.onrushers.data.repository.relation.RelationApiRepositoryImpl;
import com.onrushers.data.repository.report.ReportApiRepositoryImpl;
import com.onrushers.data.repository.reset.ResetPasswordApiRepositoryImpl;
import com.onrushers.data.repository.search.SearchApiRepositoryImpl;
import com.onrushers.data.repository.user.UserRestRepositoryImpl;
import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.AuthenticationRepository;
import com.onrushers.domain.boundaries.BoostRepository;
import com.onrushers.domain.boundaries.CommentRepository;
import com.onrushers.domain.boundaries.EventRepository;
import com.onrushers.domain.boundaries.ExploreRepository;
import com.onrushers.domain.boundaries.FeedRepository;
import com.onrushers.domain.boundaries.FileRepository;
import com.onrushers.domain.boundaries.OtherRepository;
import com.onrushers.domain.boundaries.PlaceRepository;
import com.onrushers.domain.boundaries.RelationRepository;
import com.onrushers.domain.boundaries.ReportRepository;
import com.onrushers.domain.boundaries.ResetPasswordRepository;
import com.onrushers.domain.boundaries.SearchRepository;
import com.onrushers.domain.boundaries.UserRepository;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DataModule {

	public static Context CONTEXT;

	private final Retrofit mRetrofit;

	private final Realm mRealm;

	public DataModule(Context context, String baseUrl) {
		CONTEXT = context;

		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		final OkHttpClient httpClient = new OkHttpClient.Builder()
			.addInterceptor(loggingInterceptor)
			.addInterceptor(new InvalidTokenInterceptor())
			.writeTimeout(120, TimeUnit.SECONDS)
			.readTimeout(60, TimeUnit.SECONDS)
			.connectTimeout(60, TimeUnit.SECONDS)
			.build();

		final Gson gsonConf = new GsonBuilder()
			.excludeFieldsWithoutExposeAnnotation()
			.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
			.create();

		mRetrofit = new Retrofit.Builder()
			.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
			.addConverterFactory(GsonConverterFactory.create(gsonConf))
			.baseUrl(baseUrl)
			.client(httpClient)
			.build();

		Realm.init(context);

		mRealm = Realm.getDefaultInstance();

	}

	@Provides
	Realm getRealm() {
		return mRealm;
	}

	@Provides
	AuthSessionRepository provideAuthSessionRepository(AuthSessionPrefsRepositoryImpl prefsRepo) {
		return prefsRepo;
	}

	@Provides
	AuthenticationService provideAuthenticationService() {
		return mRetrofit.create(AuthenticationService.class);
	}

	@Provides
	AuthenticationRepository provideAuthenticationRepository(
		AuthenticationRestRepositoryImpl authenticationRepository) {
		return authenticationRepository;
	}

	@Provides
	ResetPasswordService provideResetPasswordService() {
		return mRetrofit.create(ResetPasswordService.class);
	}

	@Provides
	ResetPasswordRepository provideResetPasswordRepository(
		ResetPasswordApiRepositoryImpl resetPasswordApiRepository) {
		return resetPasswordApiRepository;
	}

	@Provides
	UserService provideUserService() {
		return mRetrofit.create(UserService.class);
	}

	@Provides
	UserRepository provideUserRepository(UserRestRepositoryImpl userRepository) {
		return userRepository;
	}

	@Provides
	FeedService provideFeedService() {
		return mRetrofit.create(FeedService.class);
	}

	@Provides
	FeedRepository provideFeedRepository(FeedApiRepositoryImpl feedApiRepository) {
		return feedApiRepository;
	}

	@Provides
	FileService provideFileService() {
		return mRetrofit.create(FileService.class);
	}

	@Provides
	FileRepository provideFileRepository(FileApiRepositoryImpl fileApiRepository) {
		return fileApiRepository;
	}

	@Provides
	GoogleService provideGoogleServices() {
		return mRetrofit.create(GoogleService.class);
	}

	@Provides
	PlaceRepository providePlaceRepository(PlaceApiRepositoryImpl placeApiRepository) {
		return placeApiRepository;
	}

	@Provides
	EventService provideEventService() {
		return mRetrofit.create(EventService.class);
	}

	@Provides
	EventRepository provideEventRepository(EventApiRepositoryImpl eventApiRepository) {
		return eventApiRepository;
	}

	@Provides
	CommentService provideCommentService() {
		return mRetrofit.create(CommentService.class);
	}

	@Provides
	CommentRepository provideCommentRepository(CommentApiRepositoryImpl commentApiRepository) {
		return commentApiRepository;
	}

	@Provides
	BoostService provideBoostService() {
		return mRetrofit.create(BoostService.class);
	}

	@Provides
	BoostRepository provideBoostRepository(BoostApiRepositoryImpl boostApiRepository) {
		return boostApiRepository;
	}

	@Provides
	SearchService provideSearchService() {
		return mRetrofit.create(SearchService.class);
	}

	@Provides
	SearchRepository provideSearchRepository(SearchApiRepositoryImpl searchApiRepository) {
		return searchApiRepository;
	}

	@Provides
	ReportService provideReportService() {
		return mRetrofit.create(ReportService.class);
	}

	@Provides
	ReportRepository provideReportRepository(ReportApiRepositoryImpl reportApiRepository) {
		return reportApiRepository;
	}

	@Provides
	RelationService provideRelationService() {
		return mRetrofit.create(RelationService.class);
	}

	@Provides
	RelationRepository provideRelationRepository(RelationApiRepositoryImpl relationApiRepository) {
		return relationApiRepository;
	}

	@Provides
	ExploreService provideExploreService() {
		return mRetrofit.create(ExploreService.class);
	}

	@Provides
	ExploreRepository provideExploreRepository(ExploreApiRepositoryImpl exploreApiRepository) {
		return exploreApiRepository;
	}

	@Provides
	OtherService provideOtherService() {
		return mRetrofit.create(OtherService.class);
	}

	@Provides
	OtherRepository provideOtherRepository(OtherApiRepositoryImpl otherApiRepository) {
		return otherApiRepository;
	}
}
