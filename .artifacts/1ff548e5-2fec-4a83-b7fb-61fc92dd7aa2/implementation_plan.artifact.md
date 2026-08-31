# Fix Property Delegate Error in SettingScreen.kt

The user is encountering a compilation error in `SettingScreen.kt` at line 43:
`Property delegate must have a 'getValue(Nothing?, KProperty0<ERROR CLASS: Cannot infer argument for type parameter T>)' method.`

This error is caused by the use of a property delegate (`by`) on an unresolved or incorrectly typed expression. Specifically, `votoViewModel.tuttiIVoti` is missing from `VotoViewModel`, causing the compiler to fail to infer the type and subsequently fail to find the appropriate `getValue` extension function for the delegate.

## Proposed Changes

### [Voto Component]

#### [MODIFY] [VotoRepository.kt](file:///C:/Users/sbusti/Desktop/progetto_student_assistent/app/src/main/java/com/silvianikikarim/studentassistant/repository/VotoRepository.kt)
- Add `getAllVoti()` to expose the raw list of votes from the DAO.

#### [MODIFY] [VotoViewModel.kt](file:///C:/Users/sbusti/Desktop/progetto_student_assistent/app/src/main/java/com/silvianikikarim/studentassistant/viewmodel/VotoViewModel.kt)
- Add `tuttiIVoti` as a `StateFlow<List<Voto>>` so it can be collected in the UI.

### [UI Component]

#### [MODIFY] [SettingScreen.kt](file:///C:/Users/sbusti/Desktop/progetto_student_assistent/app/src/main/java/com/silvianikikarim/studentassistant/ui/SettingScreen.kt)
- Add explicit imports for `androidx.compose.runtime.getValue` to ensure property delegates are correctly resolved.
- The use of `tuttiIVoti` will now be resolved correctly.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:compileDebugKotlin` to verify that the compilation error is resolved.

### Manual Verification
- N/A (Compilation fix)
