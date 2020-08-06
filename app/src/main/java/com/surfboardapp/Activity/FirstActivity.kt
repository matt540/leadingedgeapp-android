package com.surfboardapp.Activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.PresenceChannel
import com.pusher.client.channel.PresenceChannelEventListener
import com.pusher.client.channel.User
import com.pusher.client.util.HttpAuthorizer
import com.surfboardapp.Adapter.UserDataAdapter
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Models.UsersData
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class FirstActivity : AppCompatActivity(), PresenceChannelEventListener {

    private lateinit var img_back: ImageView
    private lateinit var img_plus: ImageView

    private lateinit var recycler_room: RecyclerView
    private var usersArrayList: ArrayList<UsersData>? = null

    lateinit var adapter: UserDataAdapter
    private var id: String = "0"
    private var sender_id: String = "0"
    private var notification_room_id: String = "0"
    private var name: String = ""
    private var name1: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        init()
        apicall()
        click()


    }

    private fun init() {
        img_back = findViewById(R.id.img_back)
        img_plus = findViewById(R.id.img_plus)
        recycler_room = findViewById(R.id.recycler_room)
        notification_room_id = intent.getStringExtra("notification_room_id")
        usersArrayList = ArrayList()
    }

    private fun click() {

        img_plus.setOnClickListener {

            startActivity(Intent(this, SearchActivity::class.java))


        }
        img_back.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).putExtra("canceled_session", ""))
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java).putExtra("canceled_session", ""))
        finish()
    }


    private fun apicall() {
        val api: Api = Connection().getCon(this as android.content.Context)
        val dialog: Dialog = CustomLoader().loader(this)
        api.fetch_room("Bearer " + Preference().getUserData(this)?.token)
            .enqueue(object : javax.security.auth.callback.Callback,
                retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                    CustomLoader().stopLoader(dialog)
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    CustomLoader().stopLoader(dialog)

                    Log.e("da", "" + response.body())
                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            if (response.body()!!.get("data").isJsonArray) {
                                Toast.makeText(
                                    baseContext,
                                    "No Room Available Please Click on the Search Button",
                                    Toast.LENGTH_LONG
                                ).show()
                                recycler_room.visibility = View.GONE
                            }
                            if (response.body()!!.get("data").isJsonObject) {

                                var data = response.body()!!.get("data").asJsonObject

                                var jsonObject: JsonObject? = null
                                val keys: Iterator<String> = data.keySet().iterator()

                                while (keys.hasNext()) {
                                    val key = keys.next()
                                    Log.e("key", "" + key)
                                    jsonObject = data.get(key).asJsonObject

                                    var room_id = jsonObject.get("id").asString
                                    if (room_id.equals(notification_room_id)) {

                                        var userJarray1 = jsonObject.get("users").asJsonArray
                                        var messagesArray1 = jsonObject.get("messages").asJsonArray


                                        if (messagesArray1.size() > 0) {

                                            for (i in 0 until messagesArray1.size()) {

                                                val jsonObj = messagesArray1.get(i).asJsonObject
                                                id = jsonObj.get("id").asString
                                                sender_id = jsonObj.get("sender_id").asString
                                            }

                                        }

                                        if (userJarray1.size() > 0) {
                                            recycler_room.visibility = View.VISIBLE

                                            for (i in 0 until userJarray1.size()) {


                                                val jsonObj = userJarray1.get(i).asJsonObject

                                                name1 = jsonObj.get("name").asString
                                                var id = jsonObj.get("id").asString
                                                var role = jsonObj.get("role").asString
                                                var image = jsonObj.get("image").asString

                                            }


                                            startActivity(
                                                Intent(
                                                    this@FirstActivity,
                                                    ChatActivity::class.java
                                                ).putExtra("roomId", notification_room_id).putExtra(
                                                    "id",
                                                    id
                                                ).putExtra("name", name1)
                                            )
                                            finish()

                                        }
                                    }
                                    var userJarray = jsonObject.get("users").asJsonArray
                                    var messagesArray = jsonObject.get("messages").asJsonArray


                                    if (messagesArray.size() > 0) {

                                        for (i in 0 until messagesArray.size()) {

                                            val jsonObj = messagesArray.get(i).asJsonObject
                                            id = jsonObj.get("id").asString
                                            sender_id = jsonObj.get("sender_id").asString
                                        }

                                    }

                                    if (userJarray.size() > 0) {
                                        recycler_room.visibility = View.VISIBLE

                                        for (i in 0 until userJarray.size()) {


                                            val jsonObj = userJarray.get(i).asJsonObject

                                            name = jsonObj.get("name").asString
                                            var id = jsonObj.get("id").asString
                                            var role = jsonObj.get("role").asString
                                            var image = jsonObj.get("image").asString

                                            val userData = UsersData(
                                                name,
                                                id,
                                                role,
                                                image,
                                                room_id

                                            )
                                            usersArrayList!!.add(userData)
                                        }




                                        adapter = UserDataAdapter(
                                            usersArrayList!!, id, sender_id
                                        )
                                        recycler_room.adapter = adapter
                                        recycler_room.layoutManager =
                                            LinearLayoutManager(baseContext)

                                        recycler_room.addItemDecoration(
                                            DividerItemDecoration(
                                                recycler_room.context,
                                                DividerItemDecoration.VERTICAL
                                            )
                                        )

                                        adapter.notifyDataSetChanged()


                                    }
                                }
                                for (i in 0 until usersArrayList!!.size) {

                                    val id = usersArrayList!!.get(i).room_id

                                    setupPusher(id)

                                }

                            }

                        }
                    }
                    if (response.code() == 401) {

                        CustomMethods().logOutUser(this@FirstActivity)

                    }

                }
            })

    }

    private fun setupPusher(id: String) {
        val options = PusherOptions()
        options.isEncrypted = true
        var authorizer =
            HttpAuthorizer("https://linkaffiliateapp.com/api/broadcasting/auth") // live creds
//        var authorizer = HttpAuthorizer("http://surfboard-api.ebizwork.com/api/broadcasting/auth") //test creds
        var parameters = HashMap<String, String>()
        parameters.put("Authorization", "Bearer " + Preference().getUserData(this)?.token)
        authorizer.setHeaders(parameters)
        options.authorizer = authorizer
        options.setCluster("us2") //live creds
//        options.setCluster("mt1") //test creds


        val pusher = Pusher("05264e54724e00dac390", options) //live creds
//        val pusher = Pusher("2ee8cbc973b01e744920", options) //test creds
        pusher.connect()
        val channel: PresenceChannel =
            pusher.subscribePresence("presence-chat." + id, this, "message.new")
//        val channel = pusher.subscribe("presense-chat." + id)
//        channel.bind("message.new", { channelName, eventName, data ->
//
//            Log.e("data",""+data)
//
//        })
//        channel.bind("message.new", SubscriptionEventListener { channelName, eventName, data ->  })


        //        channel.bind("message.new") { channelName, eventName, data ->
//
//            Log.e("channelname",channelName)
//            Log.e("eventName",eventName)
//            Log.e("data",data)
//            val jsonObject = JSONObject(data)
//
//            val message = Message(
//                    jsonObject["user"].toString(),
//                    jsonObject["message"].toString(),
//                    jsonObject["time"].toString().toLong()
//            )
//
//            runOnUiThread {
//                adapter.addMessage(message)
//                // scroll the RecyclerView to the last added element
//                messageList.scrollToPosition(adapter.itemCount - 1);
//            }
//
//        }

    }

    override fun onUsersInformationReceived(channelName: String?, users: MutableSet<User>?) {

    }

    override fun userUnsubscribed(channelName: String?, user: User?) {
    }

    override fun userSubscribed(channelName: String?, user: User?) {
    }

    override fun onEvent(channelName: String?, eventName: String?, data: String?) {
    }

    override fun onAuthenticationFailure(message: String?, e: Exception?) {
    }

    override fun onSubscriptionSucceeded(channelName: String?) {
        Log.e("subsuccess", "" + channelName)
    }

}
