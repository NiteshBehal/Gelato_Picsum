package com.gelato.picsum.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gelato.picsum.data.models.ImageData
import com.gelato.picsum.data.network.ImageListApi
import retrofit2.HttpException
import java.io.IOException

class ImagePagingSource(val api: ImageListApi) :
    PagingSource<Int, ImageData>() {
    override fun getRefreshKey(state: PagingState<Int, ImageData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageData> {
        val position = params.key ?: 1
        return try {
            val response = api.getImageList(position)
            val nextKey = if (response.isEmpty()) {
                null
            } else {
                position + 1
            }
            LoadResult.Page(
                data = response,
                prevKey = if (position == 1) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}