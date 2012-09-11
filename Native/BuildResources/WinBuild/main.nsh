; Script generated by the HM NIS Edit Script Wizard.

; HM NIS Edit Wizard helper defines
!define PRODUCT_NAME "Tellervo"
!define PRODUCT_VERSION "${project.version}"
!define PRODUCT_PUBLISHER "Cornell University"
!define PRODUCT_WEB_SITE "http://www.tellervo.org"
!define PRODUCT_DIR_REGKEY "Software\Microsoft\Windows\CurrentVersion\App Paths\Tellervo.exe"
!define PRODUCT_UNINST_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_NAME}"
!define PRODUCT_UNINST_ROOT_KEY "HKLM"
!define MULTIUSER_EXECUTIONLEVEL Admin
!include MultiUser.nsh

; MUI 1.67 compatible ------
!include "MUI.nsh"

!include "EnvVarUpdate.nsh"

Function .onInit
  !insertmacro MULTIUSER_INIT
FunctionEnd

; MUI Settings
!define MUI_ABORTWARNING
!define MUI_ICON "../../../src/main/resources/Icons/tellervo-install.ico"
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall.ico"

; Welcome page
!insertmacro MUI_PAGE_WELCOME
; License page
!insertmacro MUI_PAGE_LICENSE "../../../src/main/resources/Licenses/license.txt"
; Directory page
!insertmacro MUI_PAGE_DIRECTORY
; Instfiles page
!insertmacro MUI_PAGE_INSTFILES
; Finish page
!define MUI_FINISHPAGE_RUN "$INSTDIR\${PRODUCT_NAME}-${PRODUCT_VERSION}.exe"
!insertmacro MUI_PAGE_FINISH

; Uninstaller pages
!insertmacro MUI_UNPAGE_INSTFILES

; Language files
!insertmacro MUI_LANGUAGE "English"

; MUI end ------

Name "${PRODUCT_NAME} ${PRODUCT_VERSION}"
OutFile "..\..\binaries\desktop\${PRODUCT_VERSION}\${OUTFOLDER}\tellervo-${PRODUCT_VERSION}-${PLATFORM_SUFFIX}setup.exe"

InstallDir "$PROGRAMFILES\${PRODUCT_NAME}"
InstallDirRegKey HKLM "${PRODUCT_DIR_REGKEY}" ""
ShowInstDetails show
ShowUnInstDetails show

Section "MainSection" SEC01
  SetOutPath "$INSTDIR"
  SetOverwrite ifnewer
  File "..\..\${PRODUCT_NAME}-${PRODUCT_VERSION}.exe"
  CreateDirectory "$SMPROGRAMS\Tellervo"
  CreateShortCut "$SMPROGRAMS\Tellervo\Tellervo.lnk" "$INSTDIR\${PRODUCT_NAME}-${PRODUCT_VERSION}.exe"
  CreateShortCut "$DESKTOP\Tellervo.lnk" "$INSTDIR\${PRODUCT_NAME}-${PRODUCT_VERSION}.exe"
  File "..\..\..\Native\Libraries\${PLATFORM}\gluegen-rt.dll"
  File "..\..\..\Native\Libraries\${PLATFORM}\jogl.dll"
  File "..\..\..\Native\Libraries\${PLATFORM}\jogl_awt.dll"
  File "..\..\..\Native\Libraries\${PLATFORM}\jogl_cg.dll"
  File "..\..\..\Native\Libraries\${PLATFORM}\rxtxSerial.dll"
  ${EnvVarUpdate} $0 "CLASSPATH" "A" "HKLM" "$INSTDIR\jogl.jar" ; Append  
SectionEnd

Section -AdditionalIcons
  CreateShortCut "$SMPROGRAMS\Tellervo\Uninstall.lnk" "$INSTDIR\uninst.exe"
  WriteINIStr "$SMPROGRAMS\Tellervo\Tutorial videos.url" "InternetShortcut" "URL" "http://www.tellervo.org/screencasts/"
SectionEnd

Section -Post
  WriteUninstaller "$INSTDIR\uninst.exe"
  WriteRegStr HKLM "${PRODUCT_DIR_REGKEY}" "" "$INSTDIR\${PRODUCT_NAME}-${PRODUCT_VERSION}.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayName" "$(^Name)"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "UninstallString" "$INSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayIcon" "$INSTDIR\${PRODUCT_NAME}-${PRODUCT_VERSION}.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayVersion" "${PRODUCT_VERSION}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "URLInfoAbout" "${PRODUCT_WEB_SITE}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "Publisher" "${PRODUCT_PUBLISHER}"
SectionEnd


Function un.onUninstSuccess
  HideWindow
  MessageBox MB_ICONINFORMATION|MB_OK "$(^Name) was successfully removed from your computer."
FunctionEnd

Function un.onInit
  !insertmacro MULTIUSER_UNINIT
  MessageBox MB_ICONQUESTION|MB_YESNO|MB_DEFBUTTON2 "Are you sure you want to completely remove $(^Name) and all of its components?" IDYES +2
  Abort
FunctionEnd

Section Uninstall
  Delete "$INSTDIR\uninst.exe"
  Delete "$INSTDIR\rxtxSerial.dll"
  Delete "$INSTDIR\gluegen-rt.dll"
  Delete "$INSTDIR\jogl.dll"
  Delete "$INSTDIR\jogl_awt.dll"
  Delete "$INSTDIR\jogl_cg.dll"
  Delete "$INSTDIR\${PRODUCT_NAME}-${PRODUCT_VERSION}.exe"

  Delete "$SMPROGRAMS\Tellervo\Uninstall.lnk"
  Delete "$DESKTOP\Tellervo.lnk"
  Delete "$SMPROGRAMS\Tellervo\Tellervo.lnk"
  Delete "$SMPROGRAMS\Tellervo\Tutorial videos.url"

  RMDir "$SMPROGRAMS\Tellervo"
  RMDir "$INSTDIR"

  DeleteRegKey ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}"
  DeleteRegKey HKLM "${PRODUCT_DIR_REGKEY}"
    
  ${un.EnvVarUpdate} $0 "CLASSPATH" "R" "HKLM" "$INSTDIR\jogl.jar"      ; Remove path of latest rev 
  SetAutoClose true
SectionEnd
