$filepath=$cmdLine[1]

ControlFocus("Open","","Scintilla1")
ControlSetText("Open","","Edit1",$filepath)
ControlClick("Open","","Button1")