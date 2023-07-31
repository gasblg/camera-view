package com.github.gasblg.cameraview


import android.R.attr.value
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import java.lang.Math.abs
import kotlin.math.roundToInt


class CameraView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val border = 3f
    private val borderForScroll = 1.5f

    private var nameTextColor = 0
    private var modelTextColor = 0
    private var objectiveTextColor = 0
    private var settingsTextColor = 0

    private var bodyColor = 0
    private var buttonColor = 0
    private var caseColor = 0
    private var borderColor = 0
    private var modelColor = 0
    private var glassColor = 0
    private var holeColor = 0

    private var showSettings = true

    private var name: String? = ""
    private var modelName: String? = ""
    private var description: String? = ""

    private var maxApertureRadius = 0f
    private var minApertureRadius = 0f
    private var step = 0f

    var rectOfButton = RectF()
    var rectOfApertureScroll = RectF()
    var rectOfExposureScroll = RectF()

    private var numberOfShots = 0
    private val maxNumberOfShots = 36
    private var aperture = Utils.getApertures()[Utils.getApertures().lastIndex]
    var scaleOfAperture = 0f
    var scaleOfExposure = 0f
    var canTakeShoot = true
    private var center = PointF(0f, 0f)

    var halfHeightCamera = 0f
    var halfWidthCamera = 0f

    private var anchorX = 0F
    private var anchorY = 0F

    var buttonY = 0f

    var apertureScrollX = 0f
    var exposureScrollX = 0f

    var radius = 0f
    var radiusOfShot = 0f

    var objectAnimator: ObjectAnimator? = null
    private var exposure = Utils.getExposure().ceilingEntry(10f)

    private val ellipsis = "..."

    private var shotListener: ShotListener? = null

    fun setShotListener(shotListener: ShotListener?) {
        this.shotListener = shotListener
    }

    companion object {
        const val MIN_DISTANCE = 150
    }

    private val paintOfName = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val paintOfBody = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val paintOfCase = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val paintOfGlass = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val paintOfModel = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val paintOfTextModel = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val paintOfTextObjective = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val paintOfHole = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val paintOfBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        strokeWidth = border
    }

    private val paintOfScrollBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        strokeWidth = borderForScroll
    }

    private val paintOfDescription = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val paintOfButton = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val paintOfShot = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val paintOfSettings = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.LEFT
        isAntiAlias = true
    }

    init {
        if (attrs != null) {
            val arrayAttrs = context.obtainStyledAttributes(attrs, R.styleable.CameraView)
            name = arrayAttrs.getString(R.styleable.CameraView_name) ?: ""
            modelName = arrayAttrs.getString(R.styleable.CameraView_model_name) ?: ""
            description = arrayAttrs.getString(R.styleable.CameraView_description) ?: ""

            nameTextColor = arrayAttrs.getColor(R.styleable.CameraView_name_text_color, 0)
            modelTextColor = arrayAttrs.getColor(R.styleable.CameraView_model_text_color, 0)
            objectiveTextColor = arrayAttrs.getColor(R.styleable.CameraView_objective_text_color, 0)
            settingsTextColor = arrayAttrs.getColor(R.styleable.CameraView_settings_text_color, 0)

            bodyColor = arrayAttrs.getColor(R.styleable.CameraView_body_color, Color.rgb(189, 189, 189))
            buttonColor = arrayAttrs.getColor(R.styleable.CameraView_button_color, 0)
            caseColor = arrayAttrs.getColor(R.styleable.CameraView_case_color, 0)
            borderColor = arrayAttrs.getColor(R.styleable.CameraView_border_color,  Color.rgb(0, 0, 0))
            modelColor = arrayAttrs.getColor(R.styleable.CameraView_model_color, 0)
            glassColor = arrayAttrs.getColor(R.styleable.CameraView_glass_color, 0)
            holeColor = arrayAttrs.getColor(R.styleable.CameraView_hole_color, 0)
            showSettings = arrayAttrs.getBoolean(R.styleable.CameraView_show_settings, true)

            paintOfName.color = nameTextColor
            paintOfTextModel.color = modelTextColor
            paintOfTextObjective.color = objectiveTextColor
            paintOfSettings.color = settingsTextColor

            paintOfBody.color = bodyColor
            paintOfButton.color = buttonColor
            paintOfCase.color = caseColor
            paintOfBorder.color = borderColor
            paintOfModel.color = modelColor
            paintOfGlass.color = glassColor
            paintOfHole.color = holeColor
            paintOfScrollBorder.color = borderColor
            paintOfShot.color = bodyColor
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        val aspect = getAspect(width.toFloat(), height.toFloat())
        val normalAspect = getNormalAspect()

        if (aspect > normalAspect) {
            if (widthMode != MeasureSpec.EXACTLY) {
                width = (normalAspect * height).roundToInt()
            }
        }

        if (aspect < normalAspect) {
            if (heightMode != MeasureSpec.EXACTLY) {
                height = (width / normalAspect).roundToInt()
            }
        }
        center.set(width / 2f, height / 2f)

        val parameters = getParametersWithAspect(width.toFloat(), height.toFloat())

        halfHeightCamera = getHalfHeightCamera(parameters.height)
        buttonY = center.y - halfHeightCamera - halfHeightCamera * 0.25f
        scaleOfAperture = getScaleOfAperture(parameters.width)
        val halfWidthCamera = getHalfWidthCamera(parameters.width)

        apertureScrollX = center.x + halfWidthCamera * 0.6f
        exposureScrollX = center.x + halfWidthCamera * 0.375f

        maxApertureRadius = getRadiusOfObjective(parameters.height) * 0.85f * 0.7f
        minApertureRadius = getRadiusOfObjective(parameters.height) * 0.85f * 0.05f

        step = (maxApertureRadius - minApertureRadius) / Utils.getApertures().size
        val calculatedRadius = maxApertureRadius - step * aperture - 1

        radius =
            if (aperture != Utils.getApertures()[Utils.getApertures().lastIndex]) calculatedRadius else minApertureRadius

        setMeasuredDimension(width, height)
    }


    private fun getAspect(w: Float, h: Float) = w / h
    private fun getNormalAspect() = 2f / 1f
    private fun getParametersWithAspect(w: Float, h: Float): Parameters {
        val aspect = getAspect(w, h)
        val normalAspect = getNormalAspect()
        var weight = w
        var height = h

        if (aspect >= normalAspect) {
            height = h * 0.85f
            weight = normalAspect * height
        }
        if (aspect < normalAspect) {
            height = w / normalAspect
        }
        return Parameters(weight, height)
    }

    private fun getWidthCamera(width: Float) = width * 0.8f
    private fun getHeightCamera(height: Float) = height * 0.7f
    private fun getHalfWidthCamera(width: Float) = getWidthCamera(width) / 2
    private fun getHalfHeightCamera(height: Float) = getHeightCamera(height) / 2
    private fun getRadiusOfObjective(height: Float) =
        getHalfHeightCamera(height) * 0.9f

    private fun getScaleOfAperture(width: Float) = width * 0.02f
    private fun getScaleOfExposure(width: Float) = width * 0.015f

    data class Parameters(val width: Float, val height: Float)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        center.set(w / 2f, h / 2f)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        if (center.x == 0F || center.y == 0F) {
            center = PointF(width / 2F, height / 2F)
        }
        val parameters = getParametersWithAspect(width.toFloat(), height.toFloat())

        halfWidthCamera = getHalfWidthCamera(parameters.width)
        halfHeightCamera = getHalfHeightCamera(parameters.height)

        val radiusOfObjective = getRadiusOfObjective(parameters.height)

        scaleOfAperture = getScaleOfAperture(parameters.width)
        scaleOfExposure = getScaleOfExposure(parameters.width)

        paintOfName.textSize = halfWidthCamera / 10f
        paintOfTextModel.textSize = halfWidthCamera / 12f

        paintOfTextObjective.textSize = halfWidthCamera / 30f
        paintOfDescription.textSize = halfWidthCamera / 25f
        paintOfSettings.textSize = halfWidthCamera / 20f

        drawBody(halfWidthCamera, halfHeightCamera, radiusOfObjective, canvas)
        if (showSettings) {
            drawSettings(halfWidthCamera, halfHeightCamera, canvas)
        }

    }

    private fun drawBody(
        halfWidthCamera: Float,
        halfHeightCamera: Float,
        radiusOfObjective: Float,
        canvas: Canvas?
    ) {

        drawButton(halfWidthCamera, halfHeightCamera, canvas)
        drawShoe(halfWidthCamera, halfHeightCamera, canvas)
        drawScrollOfAperture(halfWidthCamera, halfHeightCamera, canvas)
        drawScrollOfExposure(halfWidthCamera, halfHeightCamera, canvas)

        val rect = RectF(
            center.x - halfWidthCamera,
            center.y - halfHeightCamera,
            center.x + halfWidthCamera,
            center.y + halfHeightCamera,
        )

        canvas?.drawRect(rect, paintOfBody)
        canvas?.drawRect(rect, paintOfBorder)

        drawCase(halfWidthCamera, halfHeightCamera, canvas)
        drawModel(halfWidthCamera, halfHeightCamera, canvas)
        drawObjective(radiusOfObjective, canvas)

    }

    private fun drawCase(
        halfWidthCamera: Float,
        halfHeightCamera: Float,
        canvas: Canvas?
    ) {
        val rectOfCase = RectF(
            center.x - halfWidthCamera * 1f,
            center.y - halfHeightCamera * 0.7f,
            center.x + halfWidthCamera * 1f,
            center.y + halfHeightCamera * 0.7f
        )
        canvas?.drawRect(rectOfCase, paintOfCase)
        canvas?.drawRect(rectOfCase, paintOfBorder)

    }

    private fun drawModel(
        halfWidthCamera: Float,
        halfHeightCamera: Float,
        canvas: Canvas?
    ) {
        val rectOfModel = RectF(
            center.x + halfWidthCamera * 0.6f,
            center.y - halfHeightCamera * 0.6f,
            center.x + halfWidthCamera * 0.95f,
            center.y - halfHeightCamera * 0.3f,
        )

        canvas?.drawRect(rectOfModel, paintOfModel)
        canvas?.drawRect(rectOfModel, paintOfBorder)


        val measuredText = paintOfTextModel.measureText(modelName)
        val modelSize = (center.x + halfWidthCamera * 0.95f) - (center.x + halfWidthCamera * 0.6f)

        modelName = if (measuredText > modelSize) ellipsis else modelName

        canvas?.drawText(
            modelName ?: "",
            center.x + halfWidthCamera * 0.775f,
            center.y - halfHeightCamera * 0.375f,
            paintOfTextModel
        )
    }


    private fun drawShoe(
        halfWidthCamera: Float,
        halfHeightCamera: Float,
        canvas: Canvas?
    ) {
        val path = Path()
        path.moveTo(
            center.x - halfWidthCamera * 0.3f,
            center.y - halfHeightCamera
        )
        path.lineTo(
            center.x - halfWidthCamera * 0.3f,
            center.y - halfHeightCamera - halfHeightCamera * 0.3f
        )
        path.lineTo(
            center.x - halfWidthCamera * 0.2f,
            center.y - halfHeightCamera - halfHeightCamera * 0.45f
        )
        path.lineTo(
            center.x + halfWidthCamera * 0.2f,
            center.y - halfHeightCamera - halfHeightCamera * 0.45f
        )
        path.lineTo(
            center.x + halfWidthCamera * 0.3f,
            center.y - halfHeightCamera - halfHeightCamera * 0.3f
        )
        path.lineTo(
            center.x + halfWidthCamera * 0.3f,
            center.y - halfHeightCamera
        )

        canvas?.drawPath(path, paintOfBody)
        canvas?.drawPath(path, paintOfBorder)


        val measuredText = paintOfName.measureText(name)
        val shoeSize = (center.x + halfWidthCamera * 0.2f) - (center.x - halfWidthCamera * 0.3f)

        name = if (measuredText > shoeSize) ellipsis else name

        canvas?.drawText(
            name ?: "",
            center.x,
            center.y - halfHeightCamera - halfHeightCamera * 0.1f,
            paintOfName
        )
    }


    private fun drawButton(
        halfWidthCamera: Float,
        halfHeightCamera: Float,
        canvas: Canvas?
    ) {
        rectOfButton = RectF(
            center.x - halfWidthCamera * 0.8f,
            buttonY,
            center.x - halfWidthCamera * 0.5f,
            center.y - halfHeightCamera,
        )

        canvas?.drawRect(rectOfButton, paintOfButton)
        canvas?.drawRect(rectOfButton, paintOfBorder)

    }

    private fun drawScrollOfAperture(
        halfWidthCamera: Float,
        halfHeightCamera: Float,
        canvas: Canvas?
    ) {
        val topRect = RectF(
            center.x + halfWidthCamera * 0.6f + scaleOfAperture * 2.75f,
            center.y - halfHeightCamera - halfHeightCamera * 0.35f,
            center.x + halfWidthCamera * 0.6f + scaleOfAperture * 4.25f,
            center.y - halfHeightCamera - halfHeightCamera * 0.3f
        )

        canvas?.drawRect(topRect, paintOfBody)
        canvas?.drawRect(topRect, paintOfBorder)

        val bottomRect = RectF(
            center.x + halfWidthCamera * 0.6f + scaleOfAperture * 1.5f,
            center.y - halfHeightCamera - halfHeightCamera * 0.05f,
            center.x + halfWidthCamera * 0.7f + scaleOfAperture * 3.5f,
            center.y - halfHeightCamera
        )
        canvas?.drawRect(bottomRect, paintOfBody)
        canvas?.drawRect(bottomRect, paintOfBorder)


        rectOfApertureScroll = RectF(
            center.x + halfWidthCamera * 0.6f,
            center.y - halfHeightCamera - halfHeightCamera * 0.3f,
            center.x + halfWidthCamera * 0.6f + scaleOfAperture * 7,
            center.y - halfHeightCamera - halfHeightCamera * 0.05f
        )

        canvas?.drawRect(rectOfApertureScroll, paintOfBody)
        canvas?.drawRect(rectOfApertureScroll, paintOfBorder)

        var x1 = apertureScrollX
        val y1 = center.y - halfHeightCamera - halfHeightCamera * 0.3f
        var x2 = apertureScrollX
        val y2 = center.y - halfHeightCamera - halfHeightCamera * 0.05f


        for (i in 0..6) {
            if (i == 0 && x1 >= center.x + halfWidthCamera * 0.6f) {
                canvas?.drawLine(x1, y1, x2, y2, paintOfScrollBorder)
            }
            x1 += scaleOfAperture
            x2 += scaleOfAperture
            if (x1 <= center.x + halfWidthCamera * 0.6f + scaleOfAperture * 7) {
                if (i == 0) {
                    canvas?.drawLine(x1, y1, x2, y2, paintOfScrollBorder)
                } else {
                    canvas?.drawLine(x1, y1, x2, y2, paintOfScrollBorder)
                }
            }
        }
    }

    private fun drawScrollOfExposure(
        halfWidthCamera: Float,
        halfHeightCamera: Float,
        canvas: Canvas?
    ) {
        val topRect = RectF(
            center.x + halfWidthCamera * 0.375f + scaleOfExposure * 1.5f,
            center.y - halfHeightCamera - halfHeightCamera * 0.5f,
            center.x + halfWidthCamera * 0.375f + scaleOfExposure * 3.5f,
            center.y - halfHeightCamera - halfHeightCamera * 0.55f
        )

        canvas?.drawRect(topRect, paintOfBody)
        canvas?.drawRect(topRect, paintOfBorder)

        val bottomRect = RectF(
            center.x + halfWidthCamera * 0.375f + scaleOfExposure * 1.25f,
            center.y - halfHeightCamera - halfHeightCamera * 0.05f,
            center.x + halfWidthCamera * 0.375f + scaleOfExposure * 3.75f,
            center.y - halfHeightCamera
        )

        canvas?.drawRect(bottomRect, paintOfBody)
        canvas?.drawRect(bottomRect, paintOfBorder)

        rectOfExposureScroll = RectF(
            center.x + halfWidthCamera * 0.375f,
            center.y - halfHeightCamera - halfHeightCamera * 0.5f,
            center.x + halfWidthCamera * 0.375f + scaleOfExposure * 5,
            center.y - halfHeightCamera - halfHeightCamera * 0.05f
        )

        canvas?.drawRect(rectOfExposureScroll, paintOfBody)
        canvas?.drawRect(rectOfExposureScroll, paintOfBorder)

        var x1 = exposureScrollX
        val y1 = center.y - halfHeightCamera - halfHeightCamera * 0.5f
        var x2 = exposureScrollX
        val y2 = center.y - halfHeightCamera - halfHeightCamera * 0.05f

        for (i in 0..4) {
            if (i == 0 && x1 >= center.x + halfWidthCamera * 0.375f) {
                canvas?.drawLine(x1, y1, x2, y2, paintOfScrollBorder)
            }
            x1 += scaleOfExposure
            x2 += scaleOfExposure
            if (x1 <= center.x + halfWidthCamera * 0.375f + scaleOfExposure * 5) {
                if (i == 0) {
                    canvas?.drawLine(x1, y1, x2, y2, paintOfScrollBorder)
                } else {
                    canvas?.drawLine(x1, y1, x2, y2, paintOfScrollBorder)
                }
            }
        }
    }

    private fun drawObjective(
        radiusOfObjective: Float,
        canvas: Canvas?
    ) {

        val radiusPercent = 0.825f

        canvas?.drawCircle(center.x, center.y, radiusOfObjective, paintOfBody)
        canvas?.drawCircle(center.x, center.y, radius, paintOfHole)

        canvas?.drawCircle(center.x, center.y, radiusOfObjective * radiusPercent, paintOfGlass)
        canvas?.drawCircle(center.x, center.y, radiusOfObjective * radiusPercent, paintOfBorder)

        canvas?.drawCircle(center.x, center.y, radiusOfObjective, paintOfBorder)

        val path = Path()
        path.addCircle(center.x, center.y, radiusOfObjective * radiusPercent, Path.Direction.CW)
        canvas?.drawCircle(center.x, center.y, radiusOfShot, paintOfShot)

        val measuredText = paintOfTextObjective.measureText(description)
        val radiusSize = 2 * radiusOfObjective * radiusPercent * Math.PI

        description = if (measuredText > radiusSize) ellipsis else description
        canvas?.drawTextOnPath(description ?: "", path, 0f, -10f, paintOfTextObjective)

    }

    private fun drawSettings(
        halfWidthCamera: Float,
        halfHeightCamera: Float,
        canvas: Canvas?
    ) {
        canvas?.drawText(
            "shot: $numberOfShots/36",
            center.x - halfWidthCamera,
            center.y + halfHeightCamera + halfHeightCamera * 0.2f,
            paintOfSettings
        )
        canvas?.drawText(
            "f: $aperture",
            center.x - halfWidthCamera + halfWidthCamera * 0.325f,
            center.y + halfHeightCamera + halfHeightCamera * 0.2f,
            paintOfSettings
        )
        canvas?.drawText(
            "shutter speed: ${exposure.value}",
            center.x - halfWidthCamera + halfWidthCamera * 0.525f,
            center.y + halfHeightCamera + halfHeightCamera * 0.2f,
            paintOfSettings
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                anchorX = event.x
                anchorY = event.y

                val touchedX = event.x
                val touchedY = event.y

                if (rectOfButton.contains(touchedX, touchedY)) {
                    if (numberOfShots == maxNumberOfShots) {
                        numberOfShots = 0
                    } else {
                        if (canTakeShoot) {
                            canTakeShoot = false
                            numberOfShots++
                            shotListener?.onPush(numberOfShots, aperture, exposure?.value ?: "")
                            setValueButton(center.y - halfHeightCamera - halfHeightCamera * 0.1f)
                        }
                    }
                }
                true
            }

            MotionEvent.ACTION_MOVE -> {
                if (canTakeShoot) {

                    if (abs(event.x - anchorX) > MIN_DISTANCE) {
                        if (rectOfApertureScroll.contains(anchorX, anchorY)) {
                            if (event.x > anchorX) {
                                setValueApertureScroll(
                                    center.x + halfWidthCamera * 0.6f + scaleOfAperture,
                                    Swipe.RIGHT
                                )
                            } else {
                                setValueApertureScroll(
                                    center.x + halfWidthCamera * 0.6f - scaleOfAperture,
                                    Swipe.LEFT
                                )
                            }
                        }
                        if (rectOfExposureScroll.contains(anchorX, anchorY)) {
                            if (event.x > anchorX) {
                                setValueExposureScroll(
                                    center.x + halfWidthCamera * 0.375f + scaleOfExposure,
                                    Swipe.RIGHT
                                )
                            } else {
                                setValueExposureScroll(
                                    center.x + halfWidthCamera * 0.375f - scaleOfExposure,
                                    Swipe.LEFT
                                )
                            }
                        }
                    }
                }
                true
            }

            MotionEvent.ACTION_UP -> true
            else -> super.onTouchEvent(event)
        }
    }


    private fun setValueButton(buttonYValue: Float) {
        objectAnimator = ObjectAnimator.ofFloat(this, "buttonY", buttonY, buttonYValue)
        objectAnimator?.duration = 150
        objectAnimator?.interpolator = DecelerateInterpolator()
        objectAnimator?.addUpdateListener { postInvalidate() }
        objectAnimator?.doOnEnd {
            objectAnimator = ObjectAnimator.ofFloat(
                this, "buttonY",
                buttonYValue,
                center.y - halfHeightCamera - halfHeightCamera * 0.25f
            )
            objectAnimator?.interpolator = DecelerateInterpolator()
            objectAnimator?.addUpdateListener { postInvalidate() }
            objectAnimator?.duration = 750
            objectAnimator?.start()

            objectAnimator = ObjectAnimator.ofFloat(
                this, "radiusOfShot",
                0f,
                radius
            )
            objectAnimator?.interpolator = DecelerateInterpolator()
            objectAnimator?.addUpdateListener { postInvalidate() }
            objectAnimator?.doOnEnd {


                objectAnimator = ObjectAnimator.ofFloat(
                    this,
                    "radiusOfShot",
                    radius,
                    radius
                )
                objectAnimator?.interpolator = DecelerateInterpolator()
                objectAnimator?.addUpdateListener { postInvalidate() }
                objectAnimator?.duration = exposure?.key?.toLong() ?: 100
                objectAnimator?.doOnEnd {
                    objectAnimator = ObjectAnimator.ofFloat(
                        this,
                        "radiusOfShot",
                        radius,
                        0f
                    )
                    objectAnimator?.interpolator = DecelerateInterpolator()
                    objectAnimator?.addUpdateListener { postInvalidate() }
                    objectAnimator?.duration = 75
                    objectAnimator?.start()
                    canTakeShoot = true
                }

                objectAnimator?.start()


            }
            objectAnimator?.duration = 75
            objectAnimator?.start()

        }
        objectAnimator?.start()

    }

    private fun setValueApertureScroll(scrollValue: Float, swipe: Swipe) {
        objectAnimator =
            ObjectAnimator.ofFloat(this, "apertureScrollX", apertureScrollX, scrollValue)
        objectAnimator?.duration = 250
        objectAnimator?.interpolator = DecelerateInterpolator()
        objectAnimator?.addUpdateListener { postInvalidate() }
        objectAnimator?.doOnEnd {
            apertureScrollX = center.x + halfWidthCamera * 0.6f
            when (swipe) {
                Swipe.RIGHT -> {
                    if (aperture > Utils.getApertures().first()) {
                        radius += step
                        val index = ((maxApertureRadius - radius) / step).toInt()
                        aperture = Utils.getApertures()[index]
                    }
                }

                Swipe.LEFT -> {
                    if (aperture < Utils.getApertures().last()) {
                        radius -= step
                        val index = ((maxApertureRadius - radius) / step).toInt()
                        aperture = Utils.getApertures()[index]
                    }
                }
            }

        }
        objectAnimator?.start()
    }

    private fun setValueExposureScroll(scrollValue: Float, swipe: Swipe) {
        objectAnimator =
            ObjectAnimator.ofFloat(this, "exposureScrollX", exposureScrollX, scrollValue)
        objectAnimator?.duration = 250
        objectAnimator?.interpolator = DecelerateInterpolator()
        objectAnimator?.addUpdateListener { postInvalidate() }
        objectAnimator?.doOnEnd {
            exposureScrollX = center.x + halfWidthCamera * 0.375f
            when (swipe) {
                Swipe.RIGHT -> {
                    if (exposure.key < Utils.getExposure().lastKey()) {
                        exposure = Utils.getExposure().higherEntry(exposure.key)

                    }
                }

                Swipe.LEFT -> {
                    if (exposure.key > Utils.getExposure().firstKey()) {
                        exposure = Utils.getExposure().lowerEntry(exposure.key)
                    }

                }
            }

        }
        objectAnimator?.start()
    }

    private fun setShotsValue(value: Int) {
        numberOfShots = value
        invalidate()
    }

    private fun setApertureValue(value: Float) {
        aperture = value
        invalidate()
    }

    private fun setExposureValue(value: Float) {
        exposure = Utils.getExposure().ceilingEntry(value)
        invalidate()
    }

    override fun onSaveInstanceState(): Parcelable {
        val parentState = super.onSaveInstanceState()
        val savedState = SavedState(parentState)
        savedState.numberOfShots = numberOfShots
        savedState.aperture = aperture
        savedState.exposure = exposure.key
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        setShotsValue(savedState.numberOfShots)
        setApertureValue(savedState.aperture)
        setExposureValue(savedState.exposure)

    }

    private class SavedState : BaseSavedState {
        var numberOfShots = 1
        var aperture = 32f
        var exposure = 10f

        constructor(superState: Parcelable?) : super(superState)
        private constructor(`in`: Parcel) : super(`in`) {
            numberOfShots = `in`.readInt()
            aperture = `in`.readFloat()
            exposure = `in`.readFloat()

        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(numberOfShots)
            out.writeFloat(aperture)
            out.writeFloat(exposure)
        }

        companion object {
            @JvmField val CREATOR: Parcelable.Creator<SavedState?> =
                object : Parcelable.Creator<SavedState?> {
                    override fun createFromParcel(`in`: Parcel): SavedState? {
                        return SavedState(`in`)
                    }

                    override fun newArray(size: Int): Array<SavedState?> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }

}