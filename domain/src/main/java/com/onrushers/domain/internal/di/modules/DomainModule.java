package com.onrushers.domain.internal.di.modules;

import com.onrushers.domain.business.interactor.auth_session.DestroyAuthSessionInteractor;
import com.onrushers.domain.business.interactor.auth_session.GetAuthSessionInteractor;
import com.onrushers.domain.business.interactor.auth_session.SaveAuthSessionInteractor;
import com.onrushers.domain.business.interactor.authentication.LogInInteractor;
import com.onrushers.domain.business.interactor.boost.AddBoostInteractor;
import com.onrushers.domain.business.interactor.boost.DeleteBoostInteractor;
import com.onrushers.domain.business.interactor.boost.GetFeedBoostsInteractor;
import com.onrushers.domain.business.interactor.comment.CreateCommentInteractor;
import com.onrushers.domain.business.interactor.comment.DeleteCommentInteractor;
import com.onrushers.domain.business.interactor.comment.GetFeedCommentsInteractor;
import com.onrushers.domain.business.interactor.event.GetEventInteractor;
import com.onrushers.domain.business.interactor.event.GetEventsInteractor;
import com.onrushers.domain.business.interactor.event.GetMyEventsInteractor;
import com.onrushers.domain.business.interactor.event.RegisterEventIndividualInteractor;
import com.onrushers.domain.business.interactor.explore.GetExplorePhotosInteractor;
import com.onrushers.domain.business.interactor.explore.GetExploreRankInteractor;
import com.onrushers.domain.business.interactor.feed.CreateFeedInteractor;
import com.onrushers.domain.business.interactor.feed.DeleteFeedInteractor;
import com.onrushers.domain.business.interactor.feed.GetFeedInteractor;
import com.onrushers.domain.business.interactor.file.DownloadFileInteractor;
import com.onrushers.domain.business.interactor.file.UploadFileInteractor;
import com.onrushers.domain.business.interactor.notification.GetNotificationsInteractor;
import com.onrushers.domain.business.interactor.other.GetSliderInteractor;
import com.onrushers.domain.business.interactor.place.SearchPlaceInteractor;
import com.onrushers.domain.business.interactor.relation.CreateRelationInteractor;
import com.onrushers.domain.business.interactor.relation.DeleteRelationInteractor;
import com.onrushers.domain.business.interactor.report.CreateReportFeedInteractor;
import com.onrushers.domain.business.interactor.reset.ResetPasswordInteractor;
import com.onrushers.domain.business.interactor.search.SearchInteractor;
import com.onrushers.domain.business.interactor.search.SearchUserInteractor;
import com.onrushers.domain.business.interactor.user.GetUserFansInteractor;
import com.onrushers.domain.business.interactor.user.GetUserFeedsInteractor;
import com.onrushers.domain.business.interactor.user.GetUserHerosInteractor;
import com.onrushers.domain.business.interactor.user.GetUserInteractor;
import com.onrushers.domain.business.interactor.user.GetUserNotificationsInteractor;
import com.onrushers.domain.business.interactor.user.GetUserWallInteractor;
import com.onrushers.domain.business.interactor.user.GetUsersInteractor;
import com.onrushers.domain.business.interactor.user.UpdateUserAvatarInteractor;
import com.onrushers.domain.business.interactor.user.UpdateUserCoverInteractor;
import com.onrushers.domain.business.interactor.user.UpdateUserInfoInteractor;
import com.onrushers.domain.business.interactor.user.UpdateUserProfileInteractor;
import com.onrushers.domain.usecases.auth_session.DestroyAuthSessionUseCase;
import com.onrushers.domain.usecases.auth_session.GetAuthSessionUseCase;
import com.onrushers.domain.usecases.auth_session.StoreAuthSessionUseCase;
import com.onrushers.domain.usecases.authentication.PostLoginUseCase;
import com.onrushers.domain.usecases.boost.CreateBoostUseCase;
import com.onrushers.domain.usecases.boost.DeleteBoostUseCase;
import com.onrushers.domain.usecases.boost.GetFeedBoostsUseCase;
import com.onrushers.domain.usecases.comment.CreateCommentUseCase;
import com.onrushers.domain.usecases.comment.DeleteCommentUseCase;
import com.onrushers.domain.usecases.comment.GetFeedCommentsUseCase;
import com.onrushers.domain.usecases.event.GetEventUseCase;
import com.onrushers.domain.usecases.event.GetEventsUseCase;
import com.onrushers.domain.usecases.event.GetMyEventsUseCase;
import com.onrushers.domain.usecases.event.RegisterEventIndividualUseCase;
import com.onrushers.domain.usecases.explore.GetExplorePhotosUseCase;
import com.onrushers.domain.usecases.explore.GetExploreRankUseCase;
import com.onrushers.domain.usecases.feed.CreateFeedUseCase;
import com.onrushers.domain.usecases.feed.DeleteFeedUseCase;
import com.onrushers.domain.usecases.feed.GetFeedUseCase;
import com.onrushers.domain.usecases.file.DownloadFileUseCase;
import com.onrushers.domain.usecases.file.UploadFileUseCase;
import com.onrushers.domain.usecases.other.GetSliderUseCase;
import com.onrushers.domain.usecases.place.SearchPlaceUseCase;
import com.onrushers.domain.usecases.relation.CreateRelationUseCase;
import com.onrushers.domain.usecases.relation.DeleteRelationUseCase;
import com.onrushers.domain.usecases.report.CreateReportFeedUseCase;
import com.onrushers.domain.usecases.reset.ResetPasswordUseCase;
import com.onrushers.domain.usecases.search.SearchUseCase;
import com.onrushers.domain.usecases.search.SearchUserUseCase;
import com.onrushers.domain.usecases.user.GetUserFansUseCase;
import com.onrushers.domain.usecases.user.GetUserFeedsUseCase;
import com.onrushers.domain.usecases.user.GetUserHerosUseCase;
import com.onrushers.domain.usecases.user.GetUserNotificationsUseCase;
import com.onrushers.domain.usecases.user.GetUserUseCase;
import com.onrushers.domain.usecases.user.GetUserWallUseCase;
import com.onrushers.domain.usecases.user.GetUsersUseCase;
import com.onrushers.domain.usecases.user.UpdateUserAvatarUseCase;
import com.onrushers.domain.usecases.user.UpdateUserCoverUseCase;
import com.onrushers.domain.usecases.user.UpdateUserInfoUseCase;
import com.onrushers.domain.usecases.user.UpdateUserProfileUseCase;

import dagger.Module;
import dagger.Provides;

@Module
public class DomainModule {

	public DomainModule() {
		// Default empty constructor
	}

	@Provides
	GetAuthSessionInteractor provideGetAuthSessionInteractor(GetAuthSessionUseCase useCase) {
		return useCase;
	}

	@Provides
	SaveAuthSessionInteractor provideSaveAuthSessionInteractor(StoreAuthSessionUseCase useCase) {
		return useCase;
	}

	@Provides
	DestroyAuthSessionInteractor provideDestroyAuthSessionInteractor(DestroyAuthSessionUseCase useCase) {
		return useCase;
	}

	@Provides LogInInteractor provideAuthenticationInteractor(PostLoginUseCase useCase) {
		return useCase;
	}

	@Provides ResetPasswordInteractor provideResetPasswordInteractor(ResetPasswordUseCase useCase) {
		return useCase;
	}

	@Provides CreateFeedInteractor provideCreateFeedInteractor(CreateFeedUseCase useCase) {
		return useCase;
	}

	@Provides GetFeedInteractor provideGetFeedInteractor(GetFeedUseCase useCase) {
		return useCase;
	}

	@Provides DeleteFeedInteractor provideDeleteFeedInteractor(DeleteFeedUseCase useCase) {
		return useCase;
	}

	@Provides UploadFileInteractor provideUploadFileInteractor(UploadFileUseCase useCase) {
		return useCase;
	}

	@Provides DownloadFileInteractor provideDownloadFileInteractor(DownloadFileUseCase useCase) {
		return useCase;
	}

	@Provides SearchPlaceInteractor provideSearchPlaceInteractor(SearchPlaceUseCase useCase) {
		return useCase;
	}

	@Provides GetUserInteractor provideGetUserInteractor(GetUserUseCase useCase) {
		return useCase;
	}

	@Provides GetUsersInteractor provideGetUsersInteractor(GetUsersUseCase useCase) {
		return useCase;
	}

	@Provides GetUserHerosInteractor provideGetUserHerosInteractor(GetUserHerosUseCase useCase) {
		return useCase;
	}

	@Provides GetUserFansInteractor provideGetUserFansInteractor(GetUserFansUseCase useCase) {
		return useCase;
	}

	@Provides
	GetUserNotificationsInteractor provideGetUserNotificationInteractor(GetUserNotificationsUseCase useCase) {
		return useCase;
	}

	@Provides
	UpdateUserInfoInteractor provideUpdateUserInfoInteractor(UpdateUserInfoUseCase useCase) {
		return useCase;
	}

	@Provides
	UpdateUserProfileInteractor provideUpdateUserProfileInteractor(UpdateUserProfileUseCase useCase) {
		return useCase;
	}

	@Provides
	UpdateUserAvatarInteractor provideUpdateUserAvatarInteractor(UpdateUserAvatarUseCase useCase) {
		return useCase;
	}

	@Provides
	UpdateUserCoverInteractor provideUpdateUserCoverInteractor(UpdateUserCoverUseCase useCase) {
		return useCase;
	}

	@Provides GetUserFeedsInteractor provideGetUserFeedsInteractor(GetUserFeedsUseCase useCase) {
		return useCase;
	}

	@Provides GetUserWallInteractor provideGetUserWallInteractor(GetUserWallUseCase useCase) {
		return useCase;
	}

	@Provides GetEventsInteractor provideGetEventsInteractor(GetEventsUseCase useCase) {
		return useCase;
	}

	@Provides GetMyEventsInteractor provideGetMyEventsInteractor(GetMyEventsUseCase useCase) {
		return useCase;
	}

	@Provides GetEventInteractor provideGetEventInteractor(GetEventUseCase useCase) {
		return useCase;
	}

	@Provides
	RegisterEventIndividualInteractor provideRegisterEventIndividualInteractor(RegisterEventIndividualUseCase useCase) {
		return useCase;
	}

	@Provides CreateCommentInteractor provideCreateCommentInteractor(CreateCommentUseCase useCase) {
		return useCase;
	}

	@Provides DeleteCommentInteractor provideDeleteCommentInteractor(DeleteCommentUseCase useCase) {
		return useCase;
	}

	@Provides
	GetFeedCommentsInteractor provideGetFeedCommentsInteractor(GetFeedCommentsUseCase useCase) {
		return useCase;
	}

	@Provides AddBoostInteractor provideAddBoostInteractor(CreateBoostUseCase useCase) {
		return useCase;
	}

	@Provides DeleteBoostInteractor provideDeleteBoostInteractor(DeleteBoostUseCase useCase) {
		return useCase;
	}

	@Provides GetFeedBoostsInteractor provideGetFeedBoostsInteractor(GetFeedBoostsUseCase useCase) {
		return useCase;
	}

	@Provides SearchInteractor provideSearchInteractor(SearchUseCase useCase) {
		return useCase;
	}

	@Provides SearchUserInteractor provideSearchUserInteractor(SearchUserUseCase useCase) {
		return useCase;
	}

	@Provides
	CreateReportFeedInteractor provideCreateReportFeedInteractor(CreateReportFeedUseCase useCase) {
		return useCase;
	}

	@Provides
	CreateRelationInteractor provideCreateRelationInteractor(CreateRelationUseCase useCase) {
		return useCase;
	}

	@Provides
	DeleteRelationInteractor provideDeleteRelationInteractor(DeleteRelationUseCase useCase) {
		return useCase;
	}

	@Provides
	GetExplorePhotosInteractor provideGetExplorePhotosInteractor(GetExplorePhotosUseCase useCase) {
		return useCase;
	}

	@Provides GetExploreRankInteractor provideGetExploreRankInter(GetExploreRankUseCase useCase) {
		return useCase;
	}

	@Provides GetSliderInteractor provideGetSliderInteractor(GetSliderUseCase useCase) {
		return useCase;
	}
}
