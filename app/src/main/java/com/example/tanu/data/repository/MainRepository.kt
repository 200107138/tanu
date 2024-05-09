package com.example.tanu.data.repository

import android.util.Log
import com.example.tanu.SessionManager
import com.example.tanu.data.models.AuthRequest
import com.example.tanu.data.models.GetChatRoomsResponse
import com.example.tanu.data.models.LoginResponse
import com.example.tanu.data.retrofit.ApiService
import com.example.tanu.data.UserHolder
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
import com.example.tanu.data.models.PostRateResponse
import com.example.tanu.data.models.PostRateRequest
import com.example.tanu.data.models.PutPostStatusRequest
import com.example.tanu.data.models.PutPostStatusResponse
import com.example.tanu.data.models.PutUserNameRequest
import com.example.tanu.data.models.PutUserNameResponse
import com.example.tanu.data.models.RegisterResponse
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class MainRepository(private val apiService: ApiService, private val sessionManager: SessionManager) {
    private val TAG = "MainRepository"
    suspend fun login(request: AuthRequest): LoginResponse? {
        try {
            val response = apiService.login(request)
            return if (response.isSuccessful) {
                val accessToken = response.body()?.accessToken
                val refreshToken = response.body()?.refreshToken
                accessToken?.let { token ->
                    refreshToken?.let { refresh ->
                        sessionManager.saveTokens(token, refresh)
                    }
                }
                UserHolder.userId = response.body()?.userId
                response.body() // Return the response body
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: ${e.message}")
            throw e
        }
    }


    suspend fun register(request: AuthRequest): RegisterResponse? {
        try {
        val response = apiService.register(request)
        return if (response.isSuccessful) {
            response.body()
        } else {
            throw Exception("Registration failed") // Handle registration failure
            null
        }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: ${e.message}")
            throw e
        }
    }

    suspend fun getChatRooms(): Response<GetChatRoomsResponse> = apiService.getChatRooms()

    suspend fun getMessages(chatRoomId: String): GetMessagesResponse? {
        try {
            val response = apiService.getMessages(chatRoomId)
            return if (response.isSuccessful) {
                response.body() // Return the response body
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: ${e.message}")
            throw e
        }
    }


    suspend fun postMessage(chatRoomId: String, receiverId: String, postId: String, messageText: String): PostMessageResponse? {
        try {
            val response = apiService.postMessage(
                PostMessageRequest(
                    messageText = messageText, // Specify named argument here
                    chatRoomId = chatRoomId,
                    postId = postId,
                    receiverId = receiverId
                )
            )
            return if (response.isSuccessful) {
                response.body() // Return the response body
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: ${e.message}")
            throw e
        }
    }
    suspend fun getPosts(): GetPostsResponse? {
        try {
            val response = apiService.getPosts()
            return if (response.isSuccessful) {
                response.body() // Return the response body
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: ${e.message}")
            throw e
        }
    }

    suspend fun getPostsByUserId(userId: String): GetPostsResponse? {
        try {
            val response = apiService.getPostsByUserId(userId)
            return if (response.isSuccessful) {
                response.body() // Return the response body
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: ${e.message}")
            throw e
        }
    }

    suspend fun getDiscussionsByPostId(postId: String): GetDiscussionsByPostIdResponse? {
        try {
            val response = apiService.getDiscussionsByPostId(postId)
            return if (response.isSuccessful) {
                response.body() // Return the response body
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: ${e.message}")
            throw e
        }
    }

    suspend fun getDiscussionCommentsById(discussionId: String): GetDiscussionCommentsByIdResponse? {
        try {
            val response = apiService.getDiscussionCommentsById(discussionId)
            return if (response.isSuccessful) {
                response.body() // Return the response body
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: ${e.message}")
            throw e
        }
    }

    suspend fun getDiscussionInfo(discussionId: String): GetDiscussionInfo? {
        try {
            val response = apiService.getDiscussionInfo(discussionId)
            return if (response.isSuccessful) {
                response.body() // Return the response body
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: ${e.message}")
            throw e
        }
    }

    suspend fun getUserInfo(userId: String): GetUserInfoResponse? {
        try {
            val response = apiService.getUserInfo(userId)
            return if (response.isSuccessful) {
                response.body() // Return the response body
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: ${e.message}")
            throw e
        }
    }

    suspend fun getAllDiscussion(): GetAllDiscussionResponse? {
        try {
            val response = apiService.getAllDiscussion()
            return if (response.isSuccessful) {
                response.body() // Return the response body
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: ${e.message}")
            throw e
        }
    }

    suspend fun postRate(request: PostRateRequest): PostRateResponse? {
        try {
            val response = apiService.postRate(request)
            return if (response.isSuccessful) {
                response.body() // Return the response body
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error rating post: ${e.message}")
            throw e
        }
    }

    suspend fun postDiscussion(request: PostDiscussionRequest): PostDiscussionResponse? {
        try {
            val response = apiService.postDiscussion(request)
            return if (response.isSuccessful) {
                response.body() // Return the response body
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error posting discussion: ${e.message}")
            throw e
        }
    }

    suspend fun postComment(request: PostCommentRequest): PostCommentResponse? {
        try {
            val response = apiService.postComment(request)
            return if (response.isSuccessful) {
                response.body() // Return the response body
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error posting discussion: ${e.message}")
            throw e
        }
    }

    suspend fun postDiscussionComment(request: PostDiscussionCommentRequest): PostDiscussionCommentResponse? {
        try {
            val response = apiService.postDiscussionComment(request)
            return if (response.isSuccessful) {
                response.body() // Return the response body
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error posting discussion: ${e.message}")
            throw e
        }
    }


    suspend fun postPost(
        files: Array<MultipartBody.Part?>,
        description: String,
        title: String,
        telDonation: Long,
        cardDonation: Long,
        categoryId: String
    ): PostPostResponse? {
        try {
          val descriptionPart = RequestBody.create("text/plain".toMediaTypeOrNull(), description)
            val titlePart = RequestBody.create("text/plain".toMediaTypeOrNull(), title)
            val telDonationPart = RequestBody.create("text/plain".toMediaTypeOrNull(), telDonation.toString())
            val cardDonationPart = RequestBody.create("text/plain".toMediaTypeOrNull(), cardDonation.toString())
            val categoryIdPart = RequestBody.create("text/plain".toMediaTypeOrNull(), categoryId)
            val response = apiService.postPost(files=files, description=descriptionPart, title=titlePart, telDonation=telDonationPart, cardDonation=cardDonationPart, categoryId=categoryIdPart)
            return if (response.isSuccessful) {
                response.body() // Return the response body
            } else {
                // If response is not successful, return an error message
                val errorMessage = "Failed to post discussion: ${response.code()} - ${response.message()}"
                Log.e(TAG, errorMessage)
                null
            }
        } catch (e: Exception) {
            // Handle network or other errors
            val errorMessage = "Error posting discussion: ${e.message}"
            Log.e(TAG, errorMessage)
            throw e
        }
    }

    suspend fun getLeaderboardPosts(): GetLeaderboardPostsResponse? {
        try {
            val response = apiService.getLeaderboardPosts()
            return if (response.isSuccessful) {
                response.body() // Return the response body
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: ${e.message}")
            throw e
        }
    }
    suspend fun getChatRoomId(postId: String, receiverId: String): GetChatRoomIdResponse? {
        try {
            val response = apiService.getChatRoomId(postId=postId, receiverId=receiverId)
            return if (response.isSuccessful) {
                response.body() // Return the response body
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: ${e.message}")
            throw e
        }
    }

    suspend fun getPostInfo(postId: String): Response<GetPostInfoResponse> = apiService.getPostInfo(postId)
    suspend fun getPostCategories(): Response<GetPostCategoriesResponse> = apiService.getPostCategories()

    suspend fun getDiscussionCategories(): Response<GetDiscussionCategoriesResponse> = apiService.getDiscussionCategories()

    suspend fun getPostsRated(): Response<GetPostsRatedResponse> = apiService.getPostsRated()

    suspend fun getUserPosts(): Response<GetUserPostsResponse> = apiService.getUserPosts()

    suspend fun getPostRating(postId: String): Response<GetPostRatingResponse> = apiService.getPostRating(postId)

    suspend fun putPostStatus(request: PutPostStatusRequest): Response<PutPostStatusResponse> = apiService.putPostStatus(request)

    suspend fun putUserName(request: PutUserNameRequest): Response<PutUserNameResponse> = apiService.putUserName(request)
}