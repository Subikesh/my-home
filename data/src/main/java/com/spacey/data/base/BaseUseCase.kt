package com.spacey.data.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> ioScope(block: suspend CoroutineScope.() -> T): T = withContext(Dispatchers.IO, block)