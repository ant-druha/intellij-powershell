package com.intellij.plugin.powershell.testFramework

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.InvocationInterceptor
import org.junit.jupiter.api.extension.ReflectiveInvocationContext
import java.lang.reflect.Method

class EdtInterceptor: InvocationInterceptor {
  override fun interceptTestMethod(
    invocation: InvocationInterceptor.Invocation<Void?>,
    invocationContext: ReflectiveInvocationContext<Method?>,
    extensionContext: ExtensionContext
  ) {
    if(invocationContext.executable?.getAnnotation(RunInEdt::class.java) != null || invocationContext.targetClass?.getAnnotation(RunInEdt::class.java) != null)
      runInEdt { invocation.proceed() }
    else invocation.proceed()
  }
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class RunInEdt
