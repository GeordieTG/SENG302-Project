## Review Process (as on Wiki)

To review a task:

- run Checkstyle, and test coverage in local IDE
- **DO NOT FIX THE CODE**... 
- report any conflict, failure test and bug to the developer
- please add review comments in gitlab review format.


- [ ] Check pipeline passes.
- [ ] Manually check ACs and/or DoD in local IDE.
- [ ] Run E2E tests locally using MariaDB (https://eng-git.canterbury.ac.nz/seng302-2023/team-400/-/wikis/Local-Host-MariaDB)
- [ ] Check javadoc and the test cases(unit, integration, end2end) when applicable.
- [ ] No new code smells with sonarqube (https://sonarqube.csse.canterbury.ac.nz/)
- [ ] 0 Checkstyle issues
- [ ] At least 80 % coverage This code does not have 80% because of x,y, and z 
