package com.example.tanu.data.repository

import android.util.Log
import com.example.tanu.SessionManager
import com.example.tanu.data.models.AuthRequest
import com.example.tanu.data.models.ChatRoomResponse
import com.example.tanu.data.models.LoginResponse
import com.example.tanu.data.retrofit.ApiService
import com.example.tanu.data.UserHolder
import com.example.tanu.data.models.GetAllDiscussionResponse
import com.example.tanu.data.models.GetChatRoomIdResponse
import com.example.tanu.data.models.GetDiscussionCommentsByIdResponse
import com.example.tanu.data.models.GetDiscussionInfo
import com.example.tanu.data.models.GetDiscussionsByPostIdResponse
import com.example.tanu.data.models.GetLeaderboardPostsResponse
import com.example.tanu.data.models.GetMessagesResponse
import com.example.tanu.data.models.GetPostInfoResponse
import com.example.tanu.data.models.GetPostsResponse
import com.example.tanu.data.models.GetReviewByPostIdAndUserIdResponse
import com.example.tanu.data.models.GetUserInfoResponse
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
import okhttp3.MultipartBody
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


    suspend fun register(request: AuthRequest) {
        val response = apiService.register(request)
        if (response.isSuccessful) {
            Log.e(TAG, "Registration ssssss")
        } else {
            throw Exception("Registration failed") // Handle registration failure
            Log.e(TAG, "Registration failed")
        }
    }

    suspend fun getChatRooms(): Response<ChatRoomResponse> = apiService.getChatRooms()

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


    suspend fun postMessage(chatRoomId: String, userId: String, postId: String, messageText: String, ): PostMessageResponse? {
        try {
            val response = apiService.postMessage(PostMessageRequest(chatRoomId, userId, postId, messageText))
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
        mediaParts: Array<MultipartBody.Part?>,
        description: String,
        title: String,
        telDonation: Long,
        cardDonation: Long
    ): PostPostResponse? {
        try {
            val response = apiService.postPost(mediaParts, description, title, telDonation, cardDonation)
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

    suspend fun getReviewByPostIdAndUserId(postId: String): GetReviewByPostIdAndUserIdResponse? {
        try {
            val response = apiService.getReviewByPostIdAndUserId(postId)
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

    suspend fun getChatRoomId(userId: String, postId: String): GetChatRoomIdResponse? {
        try {
            val response = apiService.getChatRoomId(userId, postId)
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
    suspend fun getPostInfo(postId: String): GetPostInfoResponse? {
        try {
            val response = apiService.getPostInfo(postId)
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
}