package com.surfboardapp.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.surfboardapp.Adapter.SearchContactAdapter
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Models.SearchContactModel
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Connection
import retrofit2.Call
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private lateinit var search_box: SearchView
    private lateinit var img_back_search: ImageView
    private lateinit var recycler_search_list: RecyclerView
    private var searchContactList: ArrayList<SearchContactModel>? = null
    lateinit var adapter: SearchContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        init()
        click()
    }

    private fun click() {
        search_box.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                SearchContact(query)
                return false
            }

        })
        img_back_search.setOnClickListener {
            startActivity(
                Intent(this, FirstActivity::class.java).putExtra(
                    "notification_room_id",
                    ""
                )
            )
            finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, FirstActivity::class.java).putExtra("notification_room_id", ""))
        finish()
    }

    private fun SearchContact(query: String) {
        val api = Connection().getCon(this)
        api.search_contact("Bearer " + Preference().getUserData(this)?.token, query)
            .enqueue(object : javax.security.auth.callback.Callback,
                retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            val data = response.body()!!.get("data").asJsonArray
                            if (data.size() > 0) {

                                searchContactList = ArrayList()
                                for (i in 0 until data.size()) {


                                    val jsonObj = data.get(i).asJsonObject

                                    var name = jsonObj.get("name").asString
                                    var id = jsonObj.get("id").asString
                                    var role = jsonObj.get("role").asString
                                    var image = jsonObj.get("image").asString

                                    val searchContactModel = SearchContactModel(
                                        name,
                                        id,
                                        role,
                                        image

                                    )
                                    searchContactList!!.add(searchContactModel)

                                }
                                adapter = SearchContactAdapter(
                                    searchContactList!!
                                )
                                recycler_search_list.adapter = adapter
                                recycler_search_list.layoutManager =
                                    LinearLayoutManager(baseContext)

                                adapter.notifyDataSetChanged()


                            }

                        }
                    }
                    if (response.code() == 401) {

                        CustomMethods().logOutUser(this@SearchActivity)

                    }


                }
            })
    }

    private fun init() {
        search_box = findViewById(R.id.search_box)
        recycler_search_list = findViewById(R.id.recycler_search_list)
        img_back_search = findViewById(R.id.img_back_search)
    }
}
