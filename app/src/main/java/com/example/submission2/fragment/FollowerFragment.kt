package com.example.submission2.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission2.adapter.FollowerAdapter
import com.example.submission2.data.UserItems
import com.example.submission2.databinding.FragmentFollowerBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception


class FollowerFragment(private val username: String) : Fragment() {
    companion object {
        private val TAG = FollowerFragment::class.java.simpleName
    }

    private lateinit var binding: FragmentFollowerBinding
    private lateinit var adapter: FollowerAdapter
    private val list: ArrayList<UserItems> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFollowerBinding.inflate(layoutInflater)
        adapter = FollowerAdapter(list)

        list.clear()
        Log.d(TAG, "dataUser = $username")
        getUserFollower(username)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        rvConfig()
        return (binding.root)
    }

    private fun rvConfig() {
        binding.rvFollower.layoutManager = LinearLayoutManager(binding.rvFollower.context)
        binding.rvFollower.setHasFixedSize(true)
        binding.rvFollower.addItemDecoration(
            DividerItemDecoration(
                binding.rvFollower.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun getUserFollower(uname: String) {
        showLoading(true)
        val client = AsyncHttpClient()
        client.addHeader("User-agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/users/$uname/followers"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                showLoading(false)
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val responArr = JSONArray(result)
                    for (i in 0 until responArr.length()) {
                        val responObj = responArr.getJSONObject(i)
                        val username = responObj.getString("login")
                        getUserDetail(username)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                showLoading(false)
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun getUserDetail(loginx: String) {
        showLoading(true)
        val client = AsyncHttpClient()
        client.addHeader("User-agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/users/$loginx"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                showLoading(false)
                val result = String(responseBody!!)
                Log.d(TAG, result)
                try {
                    val responObj = JSONObject(result)

                    val username = responObj.getString("login")
                    val name = responObj.getString("name")
                    val avatar = responObj.getString("avatar_url")
                    val company = responObj.getString("company")
                    val location = responObj.getString("location")
                    val repository = responObj.getString("public_repos")
                    list.add(
                        UserItems(
                            username,
                            name,
                            avatar,
                            company,
                            location,
                            repository
                        )
                    )
                    showRecyclerList()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                showLoading(false)
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : GetUserDetail Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun showRecyclerList() {
        binding.rvFollower.layoutManager = LinearLayoutManager(activity)
        binding.rvFollower.adapter = adapter
        val listUserAdapt = FollowerAdapter(list)

        listUserAdapt.setOnItemClickCallback(object : FollowerAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserItems) {
                showSelectedUser(data)
            }

        })

    }

    private fun showSelectedUser(data: UserItems) {
        Toast.makeText(
            activity,
            "@${data.username}",
            Toast.LENGTH_SHORT
        ).show()
    }


    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarFollower.visibility = View.VISIBLE
        } else {
            binding.progressBarFollower.visibility = View.GONE
        }
    }
}