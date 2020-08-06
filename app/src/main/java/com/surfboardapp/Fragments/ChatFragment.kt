package com.surfboardapp.Fragments


import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.Adapter.MessageAdapter
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Models.CalendarSession
import com.surfboardapp.Models.MessageModel
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Connection
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class ChatFragment : Fragment() {
    private var model: CalendarSession? = null
    private lateinit var recyclerChat: RecyclerView
    private lateinit var sessionId: String
    private var messageList: ArrayList<MessageModel>? = null
    lateinit var adapter: MessageAdapter
    lateinit var etSendMsg: EditText
    private lateinit var imgSendMsg: ImageView
    private val delay = 2000L
    lateinit var sessionIdApi: String
    lateinit var messageBy: String
    lateinit var message: String
    lateinit var id: String
    lateinit var user: JsonObject
    lateinit var userId: String
    lateinit var userName: String
    lateinit var userProfile: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        init()

        RepeatHelper.repeatDelayed(delay) {
            //get message from user
            if (context != null) {
                getChatMessage()
            }
        }
        if (context != null) {
            getChatMessage()
        }
        onClick()
    }

    object RepeatHelper {
        val handler = Handler()
        fun repeatDelayed(delay: Long, todo: () -> Unit) {
            handler.postDelayed(object : Runnable {
                override fun run() {
                    todo()
                    handler.postDelayed(this, delay)
                }
            }, delay)
        }
    }

    private fun getChatMessage() {
//        val dialog: Dialog = CustomLoader().loader(activity as MainActivity)
        val api = Connection().getCon(context!!)

        api.getMessage("Bearer " + Preference().getUserData(context!!)?.token, sessionId)
            .enqueue(object : Callback, retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                    CustomLoader().stopLoader(dialog)
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
//                    CustomLoader().stopLoader(dialog)
                    try {

                        if (response.isSuccessful) {
                            if (response.body() != null) {

                                Log.e("macro", "res->" + response.body())

                                val data = response.body()!!.get("data").asJsonArray

                                messageList!!.clear()
                                for (i in 0 until data.size()) {
                                    val jsonObj = data.get(i).asJsonObject
                                    if (jsonObj.has("session_id")) {
                                        if (!jsonObj.get("session_id").isJsonNull) {

                                            sessionIdApi = jsonObj.get("session_id").asString
                                        }
                                    }
                                    if (jsonObj.has("message_by")) {
                                        if (!jsonObj.get("message_by").isJsonNull) {
                                            messageBy = jsonObj.get("message_by").asString
                                        }
                                    }
                                    if (jsonObj.has("message")) {
                                        if (!jsonObj.get("message").isJsonNull) {
                                            message = jsonObj.get("message").asString
                                        }
                                    }
                                    if (jsonObj.has("id")) {
                                        if (!jsonObj.get("id").isJsonNull) {
                                            id = jsonObj.get("id").asString
                                        }
                                    }

                                    if (jsonObj.has("user")) {
                                        if (!jsonObj.get("user").isJsonNull) {
                                            user = jsonObj.get("user").asJsonObject

                                            if (user.has("id")) {
                                                if (!user.get("id").isJsonNull) {
                                                    userId = user.get("id").asString
                                                }
                                            }
                                            if (user.has("name")) {
                                                if (!user.get("name").isJsonNull) {
                                                    userName = user.get("name").asString
                                                }
                                            }
                                            if (user.has("image")) {
                                                if (!user.get("image").isJsonNull) {
                                                    userProfile = user.get("image").asString
                                                }
                                            }

                                        }
                                    }

                                    val messageModel =
                                        MessageModel(
                                            sessionIdApi,
                                            messageBy,
                                            message,
                                            id,
                                            userId,
                                            userName,
                                            userProfile
                                        )

                                    messageList!!.add(messageModel)


                                }
                                adapter = MessageAdapter(
                                    activity as MainActivity,
                                    messageList!!
                                )
                                recyclerChat.postDelayed(
                                    { recyclerChat.scrollToPosition(recyclerChat.adapter!!.itemCount - 1) },
                                    0
                                )

                                recyclerChat.adapter = adapter
                                recyclerChat.layoutManager =
                                    LinearLayoutManager(context)


                                adapter.notifyDataSetChanged()

                            }
                        }
                    } catch (e: Exception) {
                        if (context != null) {
                            Toast.makeText(context, "" + e, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })

    }

    private fun init() {
        model = (arguments!!.getSerializable("model") as CalendarSession?)!!
        sessionId = model!!.sessionId
        messageList = ArrayList()
        recyclerChat = view!!.findViewById(R.id.recyclerChat)
        etSendMsg = view!!.findViewById(R.id.etSendMsg)
        imgSendMsg = view!!.findViewById(R.id.imgSendMsg)

    }

    private fun onClick() {
        imgSendMsg.setOnClickListener {
            //send message
            sendMessage()
        }
        etSendMsg.setOnEditorActionListener { _, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                //send message
                sendMessage()
                etSendMsg.text.clear()
                CustomMethods().hideKeyboard(activity!!)
            }
            false
        }
    }

    private fun sendMessage() {
        val dialog: Dialog = CustomLoader().loader(activity as MainActivity)
        val api = Connection().getCon(context!!)
        api.sendMessage(
            "Bearer " + Preference().getUserData(context!!)?.token,
            etSendMsg.text.toString(),
            sessionId
        ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                CustomLoader().stopLoader(dialog)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                CustomLoader().stopLoader(dialog)
                try {

                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            Log.e("macro", "asd->" + response.body())


//                        val data = response.body()!!.get("data").asJsonObject
//                        val message = data.get("message").asString
//                        val session_id = data.get("session_id").asString
//                        val message_by = data.get("message_by").asString
//                        val updated_at = data.get("updated_at").asString
//                        val created_at = data.get("created_at").asString
//                        val id = data.get("id").asString

                            //get message from user
//                            getChatMessage()
                            etSendMsg.text.clear()
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "" + e, Toast.LENGTH_LONG).show()
                }
            }
        })

    }


}
