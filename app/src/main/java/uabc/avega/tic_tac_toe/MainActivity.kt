package uabc.avega.tic_tac_toe

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.Integer.parseInt
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    //Variables de los botones de juego
    lateinit var bTopRight:ImageButton
    lateinit var bTop: ImageButton
    lateinit var bTopLeft:ImageButton
    lateinit var bRight:ImageButton
    lateinit var bCenter: ImageButton
    lateinit var bLeft:ImageButton
    lateinit var bBottomRight:ImageButton
    lateinit var bBottom: ImageButton
    lateinit var bBottomLeft:ImageButton
    lateinit var mainButton:Button


    //Indicadores del turno y de finalizacion del juego
    var isPlayerTurn = true
    var isFinished = true
    var turnCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bTopRight = findViewById(R.id.topRightButton)
        bTop = findViewById(R.id.topButton)
        bTopLeft = findViewById(R.id.topLeftButton)
        bRight = findViewById(R.id.rightButton)
        bCenter = findViewById(R.id.centerButton)
        bLeft = findViewById(R.id.leftButton)
        bBottomRight = findViewById(R.id.bottomRightButton)
        bBottom = findViewById(R.id.bottomButton)
        bBottomLeft = findViewById(R.id.bottomLeftButton)
        mainButton = findViewById(R.id.button)
        isPlayerTurn = true
        isFinished = true
        turnCount = 0
    }

    //Llamado al presionar algún botón cuando es turno del jugador
    //Cambia la imagen del botón a su simbolo correspondiente
    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    public fun clickChange(view:View) {
        if (view is ImageButton && !isFinished) {
            if (view.getTag() != R.drawable.o && view.getTag() != R.drawable.x) {
                if(isPlayerTurn) {
                    view.setImageDrawable(getDrawable(R.drawable.o))
                    view.setTag(R.drawable.o)
                    isPlayerTurn =false
                    mainButton.text = "CPU"
                } else {
                    view.setImageDrawable(getDrawable(R.drawable.x))
                    view.setTag(R.drawable.x)
                    isPlayerTurn = true
                    mainButton.text = "Jugador"
                }
                turnCount++
                checkState()
                if(!isPlayerTurn && !isFinished) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(500)
                        withContext(Dispatchers.Main) {
                                cpuPlay()
                        }
                    }
                }
            }
        }
    }

    //Logica de selección de CPU
    public fun cpuPlay(){
        val botones = listOf(bTopRight,bTop,bTopLeft,bRight,bCenter,bLeft,bBottomLeft,bBottom,bBottomRight)
        val selection = Random.nextInt(0,8)
        if(botones[selection].tag == R.drawable.o || botones[selection].tag == R.drawable.x){
            cpuPlay()
        } else clickChange(botones[selection])
    }


    //El estado de cada casilla cuenta con un tag distinto al inicio
    //Al cambiar, se designa el tag de su imagen correspondiente del jugador
    @SuppressLint("SetTextI18n")
    public fun checkState(){
        val topRightState = bTopRight.getTag()
        val topState = bTop.getTag()
        val topLeftState = bTopLeft.getTag()
        val rightState = bRight.getTag()
        val centerState = bCenter.getTag()
        val leftState = bLeft.getTag()
        val bottomLeftState = bBottomLeft.getTag()
        val bottomState = bBottom.getTag()
        val bottomRightState = bBottomRight.getTag()
        if(topRightState == topState && topState == topLeftState) {
            declareWinner()
        }
        if(rightState == centerState && centerState == leftState) {
            declareWinner()
        }
        if(bottomRightState == bottomState && bottomState == bottomLeftState) {
            declareWinner()
        }
        if(topRightState == rightState && rightState == bottomRightState) {
            declareWinner()
        }
        if(topState == centerState && centerState == bottomState) {
            declareWinner()
        }
        if(topLeftState == leftState && leftState == bottomLeftState) {
            declareWinner()
        }
        if(topRightState == centerState && centerState == bottomLeftState) {
            declareWinner()
        }
        if(bottomRightState == centerState && centerState == topLeftState) {
            declareWinner()
        }
        if(turnCount == 9 &&!isFinished) {
            isFinished=true
            Toast.makeText(this,"Empate", LENGTH_SHORT).show()
            Toast.makeText(this,"Presiona el botón para reiniciar", LENGTH_SHORT).show()
            mainButton.text = "Reiniciar"
        }
    }
                                                    
    @SuppressLint("SetTextI18n")
    public fun declareWinner(){
        isFinished = true
        //isPlayerTurn cambia antes de revisar al ganador
        //Se toma el valor opuesto debido a esto
        if(!isPlayerTurn) Toast.makeText(this,"Has ganado!",LENGTH_SHORT).show()
        else Toast.makeText(this, "Lastima", LENGTH_SHORT).show()

        Toast.makeText(this,"Presiona el botón para reiniciar", LENGTH_SHORT).show()
        mainButton.text = "Reiniciar"
    }

    //Llamada al presionar el botón de inicio
    //Para iniciar/reiniciar el juego
    @SuppressLint("SetTextI18n")
    public fun start(view:View) {
        //Si se ha terminado, reiniciar
        if(isFinished){
            isFinished = false
            isPlayerTurn = true
            turnCount = 0
            restartTags()
            restartImages()
        }
        mainButton.text = "Jugador"
    }

    public fun restartImages(){
        bTopRight.setImageDrawable(null)
        bTop.setImageDrawable(null)
        bTopLeft.setImageDrawable(null)
        bRight.setImageDrawable(null)
        bCenter.setImageDrawable(null)
        bLeft.setImageDrawable(null)
        bBottomLeft.setImageDrawable(null)
        bBottom.setImageDrawable(null)
        bBottomRight.setImageDrawable(null)
    }

    public fun restartTags(){
        bTopLeft.setTag(1)
        bTop.setTag(2)
        bTopRight.setTag(3)
        bLeft.setTag(4)
        bCenter.setTag(5)
        bRight.setTag(6)
        bBottomLeft.setTag(7)
        bBottom.setTag(8)
        bBottomRight.setTag(9)
    }
}
