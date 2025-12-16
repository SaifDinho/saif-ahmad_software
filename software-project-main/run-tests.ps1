#!/usr/bin/env pwsh
cd "c:\Users\Asus\Desktop\software-project-main\software-project-main"
$output = & mvn test -Dtest=FileUserRepositoryTest,FileMediaItemRepositoryTest 2>&1
Write-Host $output
