// deleted imports

class NearMeSeekBar : FrameLayout {

    private var areMetricUnitsSelected = false
    private var distanceFormatResId = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.near_me_seekbar, this)
        areMetricUnitsSelected = areMetricUnitsSelected(context)
        distanceFormatResId = if (areMetricUnitsSelected) R.string.distance_km else R.string.distance_miles
        seekbar.max = if (areMetricUnitsSelected) 1600 else 1000
        setDisplayedProgress(100.0)
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateIndicator()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                indicator_text.visibility = View.INVISIBLE
                indicator_triangle.visibility = View.INVISIBLE
            }
        })
    }


    private fun updateIndicator() {
        indicator_text.text = context.getString(distanceFormatResId, getDisplayedProgress())
        val thumbCenterX = seekbar.thumb.bounds.exactCenterX()
        val indicatorCenterX = indicator_text.background.bounds.exactCenterX()
        val leftPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics)
        indicator_text.x = thumbCenterX - indicatorCenterX + leftPadding
        indicator_triangle.x = thumbCenterX - indicator_triangle.drawable.bounds.exactCenterX() + leftPadding

        if (indicator_text.x < 0) {
            indicator_text.x = 0f
        } else if (indicator_text.x > rootview.right - indicator_text.width) {
            indicator_text.x = rootview.right.toFloat() - indicator_text.width
        }

        if (indicator_text.visibility == View.INVISIBLE) indicator_text.visibility = View.VISIBLE
        if (indicator_triangle.visibility == View.INVISIBLE) indicator_triangle.visibility = View.VISIBLE
    }


    private val alfa : Double by lazy {
        val m = seekbar.max.toDouble()
        Math.pow(m + 1, 1 / m)
    }

    // https://www.desmos.com/calculator/cutvrnfeef
    fun getDisplayedProgress(): Double {
        return Math.pow(alfa, seekbar.progress.toDouble()) - 1
    }

    fun setDisplayedProgress(progress: Double) {
        seekbar.progress = kotlin.math.log(progress + 1, alfa).roundToInt()
    }
}