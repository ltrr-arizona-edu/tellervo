param(
    [string]$Version = "",
    [string]$AppVersion = "",
    [string]$Type = "",
    [string]$JavaHome = "",
    [string]$MainJar = "",
    [string]$MainClass = "org.tellervo.desktop.gui.Startup",
    [string]$InputDir = "",
    [string]$DestDir = "",
    [switch]$VerboseOutput
)

$ErrorActionPreference = "Stop"

function Resolve-JPackagePath {
    param([string]$RequestedJavaHome)

    if ($RequestedJavaHome) {
        $candidate = Join-Path $RequestedJavaHome "bin/jpackage"
        if ($IsWindows) {
            $candidate = Join-Path $RequestedJavaHome "bin/jpackage.exe"
        }
        if (Test-Path $candidate) {
            return $candidate
        }
    }

    $command = Get-Command "jpackage" -ErrorAction SilentlyContinue
    if ($null -ne $command) {
        return $command.Source
    }

    throw "Unable to locate jpackage. Set -JavaHome or ensure jpackage is on PATH."
}

function Get-ProjectVersion {
    param([string]$PomPath)

    $project = [xml](Get-Content -LiteralPath $PomPath)
    $version = $project.project.version
    if ([string]::IsNullOrWhiteSpace($version)) {
        throw "Unable to determine project version from $PomPath"
    }
    return $version.Trim()
}

function Get-PlatformConfig {
    if ($IsWindows) {
        return @{
            Label = "windows"
            DefaultType = "msi"
            Icon = "src/main/resources/Icons/128x128/pdf.ico"
            InstallDir = "Tellervo"
            NativeLibDir = "Native/Libraries/windows-amd64"
            ExtraArgs = @("--win-per-user-install", "--win-shortcut", "--win-menu")
        }
    }

    if ($IsMacOS) {
        return @{
            Label = "macos"
            DefaultType = "app-image"
            Icon = ""
            InstallDir = "/Applications/Tellervo"
            NativeLibDir = ""
            ExtraArgs = @()
        }
    }

    return @{
        Label = "linux"
        DefaultType = "app-image"
        Icon = "src/main/resources/Icons/128x128/tellervo-application.png"
        InstallDir = "/opt/tellervo"
        NativeLibDir = ""
        ExtraArgs = @("--linux-shortcut", "--linux-menu-group", "Science", "--linux-app-category", "Science")
    }
}

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
Push-Location $repoRoot

try {
    $pomPath = Join-Path $repoRoot "pom.xml"
    if (-not $Version) {
        $Version = Get-ProjectVersion -PomPath $pomPath
    }

    if (-not $AppVersion) {
        $AppVersion = $Version
    }

    if (-not $InputDir) {
        $InputDir = Join-Path $repoRoot "target/jpackage/input"
    }

    if (-not $DestDir) {
        $DestDir = Join-Path $repoRoot "target/jpackage/dist"
    }

    $platform = Get-PlatformConfig
    if (-not $Type) {
        $Type = $platform.DefaultType
    }

    if (-not $MainJar) {
        $mainJarCandidate = Join-Path $repoRoot ("target/tellervo-{0}.jar" -f $Version)
        if (-not (Test-Path $mainJarCandidate)) {
            throw "Main application jar not found at $mainJarCandidate. Run 'mvn -DskipTests package' first."
        }
        $MainJar = Split-Path -Leaf $mainJarCandidate
    }

    $jpackage = Resolve-JPackagePath -RequestedJavaHome $JavaHome

    if (Test-Path $InputDir) {
        Remove-Item -Recurse -Force $InputDir
    }
    if (Test-Path $DestDir) {
        Remove-Item -Recurse -Force $DestDir
    }

    New-Item -ItemType Directory -Path $InputDir | Out-Null
    New-Item -ItemType Directory -Path $DestDir | Out-Null

    Copy-Item -LiteralPath (Join-Path $repoRoot "target/$MainJar") -Destination $InputDir

    $dependencyDir = Join-Path $repoRoot "target/dependency"
    if (Test-Path $dependencyDir) {
        Get-ChildItem -LiteralPath $dependencyDir -File | ForEach-Object {
            Copy-Item -LiteralPath $_.FullName -Destination $InputDir
        }
    }

    if ($platform.NativeLibDir) {
        $nativeLibDir = Join-Path $repoRoot $platform.NativeLibDir
        if (-not (Test-Path $nativeLibDir -PathType Container)) {
            throw "Native library directory not found: $nativeLibDir"
        }
        $serialLibrary = Join-Path $nativeLibDir "rxtxSerial.dll"
        if (-not (Test-Path $serialLibrary -PathType Leaf)) {
            throw "Required Windows serial library not found: $serialLibrary"
        }
        Get-ChildItem -LiteralPath $nativeLibDir -File | ForEach-Object {
            Copy-Item -LiteralPath $_.FullName -Destination $InputDir
        }
    }

    $args = @(
        "--type", $Type,
        "--dest", $DestDir,
        "--input", $InputDir,
        "--name", "Tellervo",
        "--app-version", $AppVersion,
        "--vendor", "Laboratory of Tree-Ring Research, University of Arizona",
        "--description", "Tellervo dendrochronology desktop application",
        "--copyright", "Copyright (C) Tellervo contributors",
        "--main-jar", $MainJar,
        "--main-class", $MainClass,
        "--install-dir", $platform.InstallDir,
        "--java-options", "-Dfile.encoding=UTF-8",
        "--java-options", "-Djava.awt.headless=false"
    )

    if ($platform.NativeLibDir) {
        $args += @("--java-options", '-Djava.library.path=$APPDIR')
    }

    if ($platform.Icon) {
        $iconPath = Join-Path $repoRoot $platform.Icon
        if (Test-Path $iconPath) {
            $args += @("--icon", $iconPath)
        }
    }

    $args += $platform.ExtraArgs

    if ($VerboseOutput) {
        $args += "--verbose"
    }

    Write-Host "Packaging Tellervo with jpackage for $($platform.Label)..."
    Write-Host "$jpackage $($args -join ' ')"

    & $jpackage @args
}
finally {
    Pop-Location
}
