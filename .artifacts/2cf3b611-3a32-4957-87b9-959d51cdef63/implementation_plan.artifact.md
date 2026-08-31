# Implementation Plan - Fix Property Delegate Error in SettingScreen.kt

The user is encountering a compilation error in `SettingScreen.kt` related to a property delegate. The error message indicates that the compiler cannot infer the type for the `by` delegate, which is typically caused by an unresolved reference or missing imports for the delegate extension functions (`getValue`, `setValue`).

## Proposed Changes

### [Component Name] - UI Settings

#### [MODIFY] [SettingScreen.kt](file:///C:/Users/sbusti/Desktop/progetto_student_assistent/app/src/main/java/com/silvianikikarim/studentassistant/ui/SettingScreen.kt)

1. **Fix Unresolved Reference**: Change `votoViewModel.tuttiIVoti` to `votoViewModel.votiConMateria`. `VotoViewModel` does not contain a property named `tuttiIVoti`; the correct one for the list of votes is `votiConMateria`.
2. **Correct Data Mapping**: Update the `mediaVoti` calculation. Since `voti` will now be a list of `VotoConMateria` objects, and `VotoConMateria` contains a `voto` property which is itself a `Voto` object, we need to map to `it.voto.voto` (the actual `Int` value) to calculate the average.
3. **Add Explicit Imports**: Add explicit imports for `androidx.compose.runtime.getValue` and `androidx.compose.runtime.setValue`. While `import androidx.compose.runtime.*` is present, explicit imports can sometimes resolve ambiguity issues with property delegates in Kotlin.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:compileDebugKotlin` to verify that the project builds without the delegate error.

### Manual Verification
- N/A (Compilation fix)
