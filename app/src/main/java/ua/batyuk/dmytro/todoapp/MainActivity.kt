package ua.batyuk.dmytro.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import ua.batyuk.dmytro.todoapp.common.GenericAdapter
import ua.batyuk.dmytro.todoapp.databinding.ActivityMainBinding
import ua.batyuk.dmytro.todoapp.databinding.ItemBinding

class MainActivity : AppCompatActivity() {
    val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter: GenericAdapter<ItemBinding, Task> = GenericAdapter(
            bindingCreator = { inflater, viewGroup, _ ->
                ItemBinding.inflate(inflater, viewGroup, false)
            },
            binder = { binding: ItemBinding, item: Task, position ->
                binding.rootLayout.backgroundTintList = ContextCompat.getColorStateList(
                    this,
                    if (item.completed) R.color.color_completed_task_background else R.color.color_uncompleted_task_background
                )
                binding.title.text = item.title
                binding.checkbox.setOnCheckedChangeListener(null)
                binding.checkbox.isChecked = item.completed
                binding.checkbox.setOnCheckedChangeListener { _, isCompleted ->
                    viewModel.onCompletedChanged(position, isCompleted)
                }
            }
        )

        binding.recyclerView.adapter = adapter

        viewModel.data.observe(this, {
            adapter.updateList(it.toList())
        })
    }
}