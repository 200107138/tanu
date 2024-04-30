package com.example.tanu.data.retrofit

import com.example.tanu.data.models.LoginResponse
import com.example.tanu.data.models.RegisterResponse
import com.example.tanu.data.models.AuthRequest
import com.example.tanu.data.models.GetChatRoomsResponse
import com.example.tanu.data.models.GetAllDiscussionResponse
import com.example.tanu.data.models.GetChatRoomIdResponse
import com.example.tanu.data.models.GetDiscussionCategoriesResponse
import com.example.tanu.data.models.GetDiscussionCommentsByIdResponse
import com.example.tanu.data.models.GetDiscussionInfo
import com.example.tanu.data.models.GetDiscussionsByPostIdResponse
import com.example.tanu.data.models.GetLeaderboardPostsResponse
import com.example.tanu.data.models.GetMessagesResponse
import com.example.tanu.data.models.GetPostCategoriesResponse
import com.example.tanu.data.models.GetPostInfoResponse
import com.example.tanu.data.models.GetPostRatingResponse
import com.example.tanu.data.models.GetPostsRatedResponse
import com.example.tanu.data.models.GetPostsResponse
import com.example.tanu.data.models.GetUserInfoResponse
import com.example.tanu.data.models.GetUserPostsResponse
import com.example.tanu.data.models.PostCommentRequest
import com.example.tanu.data.models.PostCommentResponse
import com.example.tanu.data.models.PostDiscussionCommentRequest
import com.example.tanu.data.models.PostDiscussionCommentResponse
import com.example.tanu.data.models.PostDiscussionRequest
import com.example.tanu.data.models.PostDiscussionResponse
import com.example.tanu.data.models.PostMessageRequest
import com.example.tanu.data.models.PostMessageResponse
import com.example.tanu.data.models.PostPostResponse
import com.example.tanu.data.models.PostRateRequest
import com.example.tanu.data.models.PostRateResponse
import com.example.tanu.data.models.PutPostStatusRequest
import com.example.tanu.data.models.PutPostStatusResponse
import com.example.tanu.data.models.PutUserNameRequest
import com.example.tanu.data.models.PutUserNameResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("/login")
    suspend fun login(@Body request: AuthRequest): Response<LoginResponse>

    @POST("/register")
    suspend fun register(@Body request: AuthRequest): Response<RegisterResponse>

    @GET("/getChatRooms")
    suspend fun getChatRooms(): Response<GetChatRoomsResponse>

    @GET("/getMessages")
    suspend fun getMessages(@Query("chatRoomId") chatRoomId: String): Response<GetMessagesResponse>

    @POST("/postMessage")
    suspend fun postMessage(@Body request: PostMessageRequest): Response<PostMessageResponse>

    @GET("/getPosts")
    suspend fun getPosts(): Response<GetPostsResponse>

    @GET("/getPostsByUserId")
    suspend fun getPostsByUserId(@Query("userId") userId: String): Response<GetPostsResponse>

    @GET("/getDiscussionsByPostId")
    suspend fun getDiscussionsByPostId(@Query("postId") postId: String): Response<GetDiscussionsByPostIdResponse>

    @GET("/getDiscussionCommentsById")
    suspend fun getDiscussionCommentsById(@Query("discussionId") discussionId: String): Response<GetDiscussionCommentsByIdResponse>

    @GET("/getDiscussionInfo")
    suspend fun getDiscussionInfo(@Query("discussionId") discussionId: String): Response<GetDiscussionInfo>

    @GET("/user/{userId}")
    suspend fun getUserInfo(@Path("userId") userId: String): Response<GetUserInfoResponse>

    @GET("/getAllDiscussion")
    suspend fun getAllDiscussion(): Response<GetAllDiscussionResponse>

    @POST("/postRate")
    suspend fun postRate(@Body request: PostRateRequest): Response<PostRateResponse>
    @POST("/postDiscussion")
    suspend fun postDiscussion(@Body request: PostDiscussionRequest): Response<PostDiscussionResponse>

    @POST("/postComment")
    suspend fun postComment(@Body request: PostCommentRequest): Response<PostCommentResponse>

    @POST("/postDiscussionComment")
    suspend fun postDiscussionComment(@Body request: PostDiscussionCommentRequest): Response<PostDiscussionCommentResponse>

    @Multipart
    @POST("/postPost")
    suspend fun postPost(
        @Part files: Array<MultipartBody.Part?>,
        @Part("description") description: String,
        @Part("title") title: String,
        @Part("tel_donation") telDonation: Long,
        @Part("card_donation") cardDonation: Long
    ): Response<PostPostResponse>

    @GET("/getLeaderboardPosts")
    suspend fun getLeaderboardPosts(): Response<GetLeaderboardPostsResponse>
    @GET("/getChatRoomId")
    suspend fun getChatRoomId(
        @Query("postId") postId: String,
        @Query("receiverId") receiverId: String
    ): Response<GetChatRoomIdResponse>

    @GET("/getPostInfo")
    suspend fun getPostInfo(@Query("postId") postId: String): Response<GetPostInfoResponse>
    @GET("/getPostCategories")
    suspend fun getPostCategories(): Response<GetPostCategoriesResponse>

    @GET("/getDiscussionCategories")
    suspend fun getDiscussionCategories(): Response<GetDiscussionCategoriesResponse>

    @GET("/getPostsRated")
    suspend fun getPostsRated(): Response<GetPostsRatedResponse>

    @GET("/getUserPosts")
    suspend fun getUserPosts(): Response<GetUserPostsResponse>

    @GET("/getPostRating")
    suspend fun getPostRating(@Query("postId") postId: String): Response<GetPostRatingResponse>

    @PUT("/putPostStatus")
    suspend fun putPostStatus(
        @Body request: PutPostStatusRequest
    ): Response<PutPostStatusResponse>

    @PUT("/putUserName")
    suspend fun putUserName(
        @Body request: PutUserNameRequest
    ): Response<PutUserNameResponse>
}