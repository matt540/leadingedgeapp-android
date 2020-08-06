package com.surfboardapp.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.PresenceChannel
import com.pusher.client.channel.PresenceChannelEventListener
import com.pusher.client.channel.User
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import com.pusher.client.util.HttpAuthorizer
import com.surfboardapp.Adapter.MessageAdapters
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.CustomClasses.RecyclerItemTouchHelper
import com.surfboardapp.CustomClasses.RecyclerItemTouchHelper1
import com.surfboardapp.Models.MessageData
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Connection
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity(), PresenceChannelEventListener,
    RecyclerItemTouchHelper1.RecyclerItemTouchHelperListener {

    private lateinit var img_back_chat: ImageView
    private lateinit var messageList: RecyclerView
    private lateinit var txtMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var txt_user_name: TextView


    private lateinit var adapter: MessageAdapters

    private var id = "0"
    private var roomId = "0"
    private var message = ""
    private var socketId = ""
    private var last_id = ""
    private var user_name = ""
    private var bool = "0"


    private var messageArrayList: ArrayList<MessageData>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        init()

        click()

        fetchMessage(id, bool)

        messageList.setOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val llm = messageList.layoutManager as LinearLayoutManager
                var mLast = llm.findLastCompletelyVisibleItemPosition()
                var mFirst = llm.findFirstVisibleItemPosition()
                var visibleItemCount = llm.childCount
                var totalItemCount = llm.itemCount


                if (mLast == ((messageArrayList!!.size) - 1)) {
                    Log.e("id0", "" + messageArrayList!![0].id)
                    Log.e("here", "here")

                    for (i in 0 until messageArrayList!!.size) {
                        last_id = messageArrayList!!.get(i).id
                    }
                    Log.e("last_id", "" + last_id)

                    bool = "1"
                    fetchMessage(last_id, bool)
                }

            }
        })
        btnSend.setOnClickListener {
            if (txtMessage.text.isNotEmpty()) {
                val api = Connection().getCon(this)
                api.send_message(
                    "Bearer " + Preference().getUserData(this)?.token,

                    "" + txtMessage.text.toString(),
                    "" + roomId
                )
                    .enqueue(object : javax.security.auth.callback.Callback,
                        retrofit2.Callback<JsonObject> {
                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                        }

                        override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                        ) {

                            resetInput()
                            messageArrayList!!.clear()
                            Log.e("responsesend", "" + response.body())
                            if (response.isSuccessful) {
                                if (response.body() != null) {


                                    var data = response.body()!!.get("data").asJsonObject
                                    var id = data.get("id").asString
                                    var room_uuid = data.get("room_uuid").asString
                                    var sender_id = data.get("sender_id").asString
                                    var created_at = data.get("created_at").asString
                                    var message = data.get("message").asString

                                    var messageData = MessageData(
                                        id, room_uuid, message, sender_id, created_at
                                    )

                                    messageArrayList!!.add(messageData)
                                    var linearLayoutManager = LinearLayoutManager(this@ChatActivity)
                                    linearLayoutManager.reverseLayout = true
                                    messageList.layoutManager = linearLayoutManager
                                    adapter = MessageAdapters(this@ChatActivity, messageArrayList)
                                    messageList.adapter = adapter

                                    val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
                                        RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT)
                                    ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(
                                        messageList
                                    )

                                    adapter.notifyDataSetChanged()
                                }

                            }

                        }
                    })
            } else {
                Toast.makeText(
                    applicationContext,
                    "Message should not be empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        setupPusher(roomId)
    }

    private fun click() {
        img_back_chat.setOnClickListener {
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

    private fun resetInput() {
        // Clean text box
        txtMessage.text.clear()

        // Hide keyboard
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun setupPusher(roomId: String?) {


        val options = PusherOptions()
        options.isEncrypted = true
        var authorizer =
            HttpAuthorizer("https://linkaffiliateapp.com/api/broadcasting/auth") //livecreds
//        var authorizer = HttpAuthorizer("http://surfboard-api.ebizwork.com/api/broadcasting/auth")//testcreds
        var parameters = HashMap<String, String>()
        parameters.put("Authorization", "Bearer " + Preference().getUserData(this)?.token)
        authorizer.setHeaders(parameters)
        options.authorizer = authorizer
        options.setCluster("us2") // live creds
//        options.setCluster("mt1") // testCreds


        var pusher = Pusher("05264e54724e00dac390", options) //live creds
//        var pusher = Pusher("2ee8cbc973b01e744920", options) //test creds

        pusher.connect(object : ConnectionEventListener {
            override fun onConnectionStateChange(change: ConnectionStateChange) {
                socketId = pusher.connection.socketId
            }

            override fun onError(message: String?, code: String?, e: java.lang.Exception?) {
                println("There was a problem connecting!")
            }
        }, ConnectionState.CONNECTED)

        val channel: PresenceChannel =
            pusher.subscribePresence("presence-chat." + roomId, this, "message.new")

        Log.e("socketId", "" + socketId)
    }

    private fun fetchMessage(id: String, bool: String) {
        val api = Connection().getCon(this)
        api.fetch_message("Bearer " + Preference().getUserData(this)?.token, "" + roomId, "" + id)
            .enqueue(object : javax.security.auth.callback.Callback,
                retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    Log.e("responsechat", "" + response.body())
                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            var data = response.body()!!.get("data").asJsonArray

                            if (data.size() > 0) {

                                for (i in 0 until data.size()) {

                                    var jsonObject = data.get(i).asJsonObject
                                    var id = jsonObject.get("id").asString
                                    var room_uuid = jsonObject.get("room_uuid").asString
                                    var sender_id = jsonObject.get("sender_id").asString
                                    var created_at = jsonObject.get("created_at").asString
                                    if (!jsonObject.get("message").isJsonNull) {
                                        message = jsonObject.get("message").asString
                                    } else {
                                        message = ""
                                    }


                                    var messageData = MessageData(
                                        id, room_uuid, message, sender_id, created_at
                                    )
                                    messageArrayList!!.add(messageData)

                                }
//                                    Collections.reverse(messageArrayList)

                                var linearLayoutManager = LinearLayoutManager(this@ChatActivity)
                                linearLayoutManager.reverseLayout = true
                                messageList.layoutManager = linearLayoutManager
                                adapter = MessageAdapters(this@ChatActivity, messageArrayList)
                                messageList.adapter = adapter

                                val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
                                    RecyclerItemTouchHelper1(
                                        0,
                                        ItemTouchHelper.LEFT,
                                        this@ChatActivity
                                    )
                                ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(
                                    messageList
                                )

                                adapter.notifyDataSetChanged()
                            }

                        }
                    }
                    if (response.code() == 401) {

                        CustomMethods().logOutUser(this@ChatActivity)

                    }
                }
            })

    }

    private fun init() {
        img_back_chat = findViewById(R.id.img_back_chat)
        messageList = findViewById(R.id.messageList)
        txtMessage = findViewById(R.id.txtMessage)
        btnSend = findViewById(R.id.btnSend)
        txt_user_name = findViewById(R.id.txt_user_name)

        messageArrayList = ArrayList()

        roomId = intent.getStringExtra("roomId")
        id = intent.getStringExtra("id")
        user_name = intent.getStringExtra("name")

        txt_user_name.text = user_name
    }

    override fun onUsersInformationReceived(channelName: String?, users: MutableSet<User>?) {

    }

    override fun userUnsubscribed(channelName: String?, user: User?) {
    }

    override fun userSubscribed(channelName: String?, user: User?) {
    }

    override fun onEvent(channelName: String?, eventName: String?, data: String?) {

        try {


            Log.e("inner_event", "" + data)
            val jsonObject = JSONObject(data)
//
            Log.e("datajsonObject", "" + jsonObject)
            var jsonObject1 = jsonObject.getJSONObject("message")
            val message = MessageData(
                jsonObject1.getString("id"),
                jsonObject1.getString("room_uuid"),
                jsonObject1.getString("message"),
                jsonObject1.getString("sender_id"),
                jsonObject1.getString("created_at")

            )

            runOnUiThread {
                messageArrayList!!.clear()
                adapter.addMessage(message)

                // scroll the RecyclerView to the last added element
                messageList.scrollToPosition(adapter.itemCount - 1)
            }
        } catch (e: Exception) {

        }
    }

    override fun onAuthenticationFailure(message: String?, e: Exception?) {
    }

    override fun onSubscriptionSucceeded(channelName: String?) {
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
        if (viewHolder != null) {
            if (viewHolder is MessageAdapters.MyMessageViewHolder) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(resources.getString(R.string.app_name))
                builder.setMessage("Are you sure you want to Delete Message")
                //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                    adapter.removeItem(viewHolder.adapterPosition)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                    dialog.dismiss()
                    adapter.notifyDataSetChanged()
                }

                builder.show()

            }
        }
    }
}
