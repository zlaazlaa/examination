package com.example.examination.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.examination.databinding.FragmentNotificationsBinding
import com.example.examination.Setting
import com.example.examination.ShowOrders

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.setting.setOnClickListener {
            val intent = Intent(activity, Setting::class.java)
            startActivity(intent)
        }
        binding.totalOrder.setOnClickListener {
            val intent = Intent(activity, ShowOrders::class.java)
            startActivity(intent)
        }
        binding.waitToPay.setOnClickListener {
             val intent = Intent(activity, ShowOrders::class.java)
            intent.putExtra("target_id", "0")
            startActivity(intent)
        }
        binding.waitToSend.setOnClickListener {
            val intent = Intent(activity, ShowOrders::class.java)
            intent.putExtra("target_id", "1")
            startActivity(intent)
        }
        binding.sending.setOnClickListener {
            val intent = Intent(activity, ShowOrders::class.java)
            intent.putExtra("target_id", "2")
            startActivity(intent)
        }
        binding.waitToScore.setOnClickListener {
            val intent = Intent(activity, ShowOrders::class.java)
            intent.putExtra("target_id", "3")
            startActivity(intent)
        }
        binding.scored.setOnClickListener {
            val intent = Intent(activity, ShowOrders::class.java)
            intent.putExtra("target_id", "4")
            startActivity(intent)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}