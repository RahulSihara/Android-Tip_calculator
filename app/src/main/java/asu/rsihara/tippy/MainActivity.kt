package asu.rsihara.tippy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator

// Name the TAG same as class
private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
class MainActivity : AppCompatActivity() {

    // late initialization of variables as we don't want them to be on create method
    private lateinit var etBaseAmount: EditText
    private lateinit var sbTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        sbTip = findViewById(R.id.sbTip)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)

        // we set default value for tip as 15%
        sbTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)

        // we need to get notified when something happen on seekbar
        sbTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                // we wan to see the logs when we change the seekbar
                Log.i(TAG, "onProgressChanged $p1")
                tvTipPercentLabel.text = "$p1%"
                computeTipAndTotal()
                updateTipDescription(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                // Not required
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                // Not required
            }

        })

        etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //NA

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //NA
            }

            override fun afterTextChanged(p0: Editable?) {
                computeTipAndTotal()
            }


        })
    }

    private fun updateTipDescription(tippercent: Int) {
        val tipDescription = when (tippercent) {
            in 0..9 -> "Poor"
            in 10 .. 14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        tvTipDescription.text = tipDescription
        // update the color based on the tip percent
        // Usage interpolation
        var color = ArgbEvaluator().evaluate(
            tippercent.toFloat()/(sbTip.max/5),
            ContextCompat.getColor(this, R.color.color_worst),
            ContextCompat.getColor( this, R.color.color_best)

        ) as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        //check if etbaseamount is empty or not
        if (etBaseAmount.text.isEmpty()){
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }

        // 1. Get the value of base and tip percent
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = sbTip.progress

        // 2. computer tip and total
        val tipAmount= baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount

        // 3. Update the UI
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text ="%.2f".format(totalAmount)
    }
}