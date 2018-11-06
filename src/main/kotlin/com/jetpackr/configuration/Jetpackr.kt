package com.jetpackr.configuration

import com.jetpackr.configuration.container.Container
import com.jetpackr.configuration.tool.Tool
import com.jetpackr.configuration.machine.Machine

class Jetpackr(
        val machine: Machine,
        val kits: Map<String, Tool>,
        val containers: Map<String, Container>
)