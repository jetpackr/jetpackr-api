package com.jetpackr.configuration

import com.jetpackr.configuration.container.Container
import com.jetpackr.configuration.kit.Kit
import com.jetpackr.configuration.machine.Machine

class Jetpackr(
        val machine: Machine,
        val kits: Map<String, Kit>,
        val containers: Map<String, Container>
)