mkdir FSim
mkdir "FSim/ProjectSettings"
xcopy "Editor/ProjectSettings" "FSim/ProjectSettings"
cd FSim
mklink /D Assets ..\Editor\Assets
cd ..