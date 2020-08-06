package baaahs

import baaahs.DeadCodeEliminationDefeater.noDCE
import baaahs.browser.RealMediaDevices
import baaahs.client.WebClient
import baaahs.jsx.sim.MosaicApp
import baaahs.model.ObjModel
import baaahs.net.BrowserNetwork
import baaahs.net.BrowserNetwork.BrowserAddress
import baaahs.sim.ui.WebClientWindow
import kotlinext.js.jsObject
import org.w3c.dom.get
import react.createElement
import react.dom.render
import kotlin.browser.document
import kotlin.browser.window

fun main(args: Array<String>) {
    @Suppress("ConstantConditionIf", "SimplifyBooleanWithConstants")
    if (1 + 1 == 3) noDCE()

    val mode = document["sparklemotionMode"] ?: "test"
    println("args = $args, mode = $mode")

    val pinkyAddress = BrowserAddress(websocketsUrl())
    val network = BrowserNetwork(pinkyAddress, baaahs.proto.Ports.PINKY)
    val contentDiv = document.getElementById("content")

    when (mode) {
        "Simulator" -> {
            val simulator = SheepSimulator()
            val props = jsObject<MosaicApp.Props> {
                this.simulator = simulator
                this.webClientWindow = WebClientWindow
            }
            val simulatorEl = document.getElementById("app")
            render(createElement(MosaicApp::class.js, props), simulatorEl)
        }

        "Admin" -> {
            val adminApp = AdminUi(network, pinkyAddress)
            render(adminApp.render(), contentDiv)
        }

        "Mapper" -> {

            val model = Pluggables.getModel() // todo: not this
            (model as? ObjModel)?.load()

            val mapperUi = JsMapperUi();
            val mediaDevices = RealMediaDevices()
            val mapper = Mapper(network, model, mapperUi, mediaDevices, pinkyAddress);
            render(mapperUi.render(), contentDiv);
            mapper.start();
        }

        "UI" -> {
            val uiApp = WebClient(network, pinkyAddress)
            render(uiApp.render(), contentDiv)
        }

        "test" -> {}

        else -> throw UnsupportedOperationException("unknown mode $mode")
    }
}

private fun websocketsUrl(): String {
    val l = window.location
    val proto = if (l.protocol === "https:") "wss:" else "ws:"
    return "$proto//${l.host}/"
}