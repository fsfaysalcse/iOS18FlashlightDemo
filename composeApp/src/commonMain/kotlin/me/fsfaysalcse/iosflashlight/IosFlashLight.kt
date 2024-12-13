package me.fsfaysalcse.iosflashlight

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import iosflashlight.composeapp.generated.resources.Res
import iosflashlight.composeapp.generated.resources.ic_menu_brightness
import iosflashlight.composeapp.generated.resources.ic_torch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.max
import kotlin.math.min


@Composable
fun IosFlashLight() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {

        var isFlashOn by remember { mutableStateOf(false) }

        val animateColumnSize by animateDpAsState(
            targetValue = if (isFlashOn) 250.dp else 100.dp,
            label = "Animate Column Size"
        )

        val torchColor by animateColorAsState(
            targetValue = if (isFlashOn) Color.White else Color.Gray,
            label = "Torch Color"
        )

        Text(
            text = "Linkedin @fsfaysalcse",
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        )

        Column(
            modifier = Modifier
                .size(
                    height = animateColumnSize,
                    width = 200.dp
                )
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(30.dp)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LightBeamCanvas(
                modifier = Modifier.size(if (isFlashOn) 150.dp else 0.dp),
                isVisible = isFlashOn
            )

            Image(
                painter = painterResource(Res.drawable.ic_torch),
                modifier = Modifier
                    .size(width = 40.dp, height = 60.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        isFlashOn = !isFlashOn
                    },
                contentDescription = "Torch Light",
                contentScale = ContentScale.FillBounds,
                colorFilter = ColorFilter.tint(torchColor)
            )
        }

    }
}


/**
This composable function draws a cone shape on a canvas.
The cone has the following features:
- The cone's height and width can be adjusted by dragging.
- The top of the cone has rounded corners.
- A bending line is drawn from the top to the bottom of the cone, which adjusts based on the width of the cone.
- A brightness icon is drawn at the end of the line.

The height and width of the cone are initially set to 200f, and both the cone height and width can be modified by dragging the cone.
The cone's alpha transparency is adjusted based on the cone's height.
 */

@Composable
fun LightBeamCanvas(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    var coneHeight by remember { mutableFloatStateOf(300f) }
    var coneWidth by remember { mutableFloatStateOf(300f) }

    val brightnessIconPainter = painterResource(Res.drawable.ic_menu_brightness)

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { _, dragAmount ->
                        coneHeight = max(50f, min(300f, coneHeight - dragAmount.y))
                        coneWidth = max(50f, min(300f, coneWidth + dragAmount.x))
                    }
                )
            }
    ) {
        if (isVisible) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val centerX = canvasWidth / 2
            val bottomY = canvasHeight
            val topY = bottomY - coneHeight

            // Draw the main cone path with rounded top corners
            val path = Path().apply {
                moveTo(centerX - coneWidth / 2, topY)
                cubicTo(
                    centerX - coneWidth / 4,
                    topY,
                    centerX + coneWidth / 4,
                    topY,
                    centerX + coneWidth / 2,
                    topY
                )
                lineTo(centerX + 25f, bottomY)
                lineTo(centerX - 25f, bottomY)
                close()
            }

            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.6f),
                        Color.Transparent
                    ),
                    startY = topY,
                    endY = canvasHeight
                )
            )

            val bendingFactor = if (coneWidth > 50f) {
                min(50f, coneWidth / 2)
            } else {
                0f
            }

            val lineStartX = centerX - coneWidth / 2
            val lineEndX = centerX + coneWidth / 2
            val lineControlX = centerX
            val lineY = topY - 20f

            val bendingLinePath = Path().apply {
                moveTo(lineStartX, lineY)
                if (bendingFactor > 0f) {
                    quadraticTo(lineControlX, lineY - bendingFactor, lineEndX, lineY)
                } else {
                    lineTo(lineEndX, lineY)
                }
            }

            drawPath(
                path = bendingLinePath,
                color = Color.White,
                style = Stroke(width = 5f)
            )

            // Draw the brightness icon using a painter and translate for positioning
            val iconSize = 15.dp.toPx()
            val iconCenterX = lineEndX + iconSize / 2 + 10f
            val iconCenterY = lineY

            translate(left = iconCenterX - iconSize / 2, top = iconCenterY - iconSize / 2) {
                with(brightnessIconPainter) {
                    draw(size = Size(iconSize, iconSize))
                }
            }
        }
    }
}


@Preview()
@Composable
fun PreviewConeCanvas() {
    IosFlashLight()
}
