# Secretary mobile app
###  (Administrarea activităților Asociației Exploratori pentru Viitor)

## Descrierea cerințelor
Aplicatie cu baze de date relationale avand interfata vizuala. Această aplicație are rolul de a asigura administrarea activităților și persoanelor din cadrul Asociației Exploratori pentru viitor, care este similară în multe aspecte Cercetașilor. Aceasta este destinată pentru utilizarea de către secretariatul național.
Lista funcționalităților și componentelor: Salvarea informațiilor despre exploratori(participanții care sunt în clasa a V-a până în clasa a VII-a), a părinților acestora(exploratorii fiind minori este nevoie de acordul părinților pentru participarea la activitățile asociației), a proiectelor asociației și a structurii organizatorice a acesteia la nivel național. De asemenea informațiile pot ulterior modificate și se pot face prelucrări în vederea cuantificării performanțelor exploratorilor, a unităților organizatorice(cluburile și conferințele) precum și gradul de succes al proiectelor pe baza numărului de participanți și a numărului de ore petrecute voluntari în acestea.
Organizarea asociației este de formă piramidală. Mai mulți exploratori formează un club(acesta reprezentând forma de organizare la nivelul orașului), mai multe cluburi formează o conferință(fiind forma de organizare la nivelul unei zone). O conferință reprezintă toate cluburile dintr-o zonă geografică(ex: Muntenia, Oltenia, etc.). Exemple de cluburi: Ezra, Neemia, etc.
## Tabele
**Exploratori**<br/>
IDExplorator	Nume	Prenume	CNP	    NrSpecializari	Grad	Instructor	IDParinte	IDClub<br/>
Integer	        Text	Text	Text	Integer	        Text	Integer	    Integer	    Integer<br/>

DataStart	DataFinal<br/>
Text	    Text<br/>
*
**Parinti**<br/>
IDParinte	Nume	Prenume	Gen	    NrDeTelefon<br/>
Integer  	Text	Text	Text	Text<br/>

**Proiecte**<br/>
IDProiect	Nume	DataStart	DataFinal	DescriereScurtă<br/>
Integer	    Text	Text	    Text	    Text<br/>

**Cluburi**<br/>
IDClub	Nume	IDConferință	IDConducător<br/>
Integer	Text	Integer	        Integer<br/>

**Conferințe**<br/>
IDConferință	Nume	IDDirector<br/>
Integer	        Text	Integer<br/>

Tabel de legătură:<br/>
**ExploratoriInProiecte**<br/>
ID	    IDProiect	IDExplorator	NrOre<br/>
Integer	Integer	    Integer	        Integer<br/>
	
## Relații
Conferințe – Are(1:N) – Cluburi.\c\n<br/>
Cluburi –Are(1:N) – Exploratori.<br/>
Cluburi – CondusDe(1:1) – Exploratori.<br/>
Conferinte – CondusDe(1:1) – Exploratori.<br/>
Proiect – ExploratoriInProiecte(N:N) – Exploratori.<br/>
Părinți – Are(1:N) – Exploratori. Se ia în calcul un singur părinte pentru fiecare copil.<br/>
## Restrângeri și definire tabele
Mai întâi trebuie introduși părinții deoarece Exploratorii au nevoie de ID-ul acestora. Instructorii nu au nevoie să le fie specificați părinții deci nu depinde de instanțe ale Parinti putând fii introduși înainte și după aceștia. <br/>
Primary Key ar trebuie să implice NOT NULL conform standardului SQL dar pentru a permite compatibilitatea cu versiuni mai vechi se permit valori NULL pentru cheia primară (SQLite Primary Key, 2019). Ca urmare alături de Primary Key am adăugat și NOT NULL.<br/>
Exploratori<br/>
CREATE TABLE Exploratori(IDExplorator INTEGER NOT NULL primary key AUTOINCREMENT, Nume TEXT NOT NULL, Prenume TEXT NOT NULL, CNP TEXT CHECK(length(CNP) = 13) UNIQUE NOT NULL, NrSpecializari INTEGER NOT NULL, Grad TEXT CHECK(Grad IN ('ucenic','calator','navigator','mesager','ghid-asistent','ghid','master-ghid')), Instructor INTEGER NOT NULL CHECK(Instructor IN (0,1)), IDParinte INTEGER, IDClub INTEGER, DataStart TEXT NOT NULL, DataFinal TEXT NOT NULL, <br/>
     CONSTRAINT fk_exploratori_parinte<br/>
     FOREIGN KEY (IDParinte)<br/>
     REFERENCES Parinti(IDParinte)<br/>
     ON DELETE SET NULL,<br/>
     CONSTRAINT fk_exploratori_club<br/>
     FOREIGN KEY (IDClub)<br/>
     REFERENCES Cluburi(IDClub)<br/>
     ON DELETE CASCADE<br/>
    )<br/>
<br/>
Părinti<br/>
CREATE TABLE Parinti( IDParinte INTEGER NOT NULL primary key  AUTOINCREMENT, Nume TEXT NOT NULL, Prenume TEXT NOT NULL, Gen TEXT NOT NULL CHECK(Gen IN ('m','f')), NrTelefon TEXT CHECK(LENGTH(NrTelefon) = 10))<br/>
Proiecte<br/>
CREATE TABLE Proiecte(IDProiecte INTEGER NOT NULL primary key AUTOINCREMENT, Nume TEXT NOT NULL, DataStart TEXT NOT NULL, DataFinal TEXT NOT NULL, DescriereScurta TEXT NOT NULL);<br/>
<br/>
Cluburi<br/>
CREATE TABLE Cluburi3(IDClub INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Nume TEXT NOT NULL,<br/>
 IDConferinta INTEGER, IDConducator INTEGER,<br/>
 CONSTRAINT fk_cluburi_conferinte<br/>
 FOREIGN KEY (IDConferinta)<br/>
 REFERENCES Conferinte(IDConferinta)<br/>
 ON DELETE CASCADE,<br/>
 CONSTRAINT fk_cluburi_exploratori<br/>
 FOREIGN KEY (IDConducator)<br/>
 REFERENCES Exploratori(IDExplorator)<br/>
 ON DELETE SET NULL<br/>
 );<br/>
<br/>
Conferinte<br/>
CREATE TABLE Conferinte( IDConferinta INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Nume TEXT NOT NULL, IDDirector INTEGER NOT NULL,<br/>
 CONSTRAINT fk_conferinte_exploratori<br/>
 FOREIGN KEY (IDDirector)<br/>
 REFERENCES Exploratori(IDExplorator)<br/>
 ON DELETE SET NULL);<br/>
<br/>
ExploratoriInProiecte<br/>
CREATE TABLE ExploratoriInProiect( ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, <br/>
IDProiect INTEGER NOT NULL, IDExplorator INTEGER NOT NULL, NrOre INTEGER NOT NULL,<br/>
CONSTRAINT fk_exploinproiect_proiecte <br/>
FOREIGN KEY (IDProiect)<br/>
REFERENCES Proiecte(IDProiecte)<br/>
ON DELETE CASCADE,<br/>
CONSTRAINT fk_exploinproiect_exploratori<br/>
FOREIGN KEY (IDExplorator)<br/>
REFERENCES Exploratori(IDExplorator)<br/>
ON DELETE CASCADE<br/>
);<br/>

## Funcționarea aplicației
 
Prima pagină are 3 butoane care permit alegerea funției dorite: de adăugare înregistrări, căutare și modificare a înregistrărilor sau ștergere. Interogările simple și cele complexe pot fi găsite dând click pe butonul căutare, iar la Modifică/Șterge pot fi doar vizionate informațiile introduse din toate tabelele, modificate sau șterse.<br/>
 
Atunci când se dă click pe una din funcții apare o pagină care permite alegerea tabelului pe care dorim 
să-l modificăm.<br/>
  
Aceasta este o pagină de introducere informații despre explorator.<br/>
 
Pagina cu interogări simple este ușor de utilizat, alegerea parametrilor pentru interogării putând fi făcută numai cu valori deja existente în baza de date.<br/>
## Interogări Simple
Interogările pot fi găsite în Cautator.java numele tabelelor fiind date sub forma de constante pentru a putea facilita o modificare ușoară a numelor ulterior.<br/>
## #Interogarea 1
SELECT E.Nume, E.Prenume, E.IDExplorator FROM Exploratori E INNER JOIN Cluburi C ON E.IDClub == C.IDClub INNER JOIN Conferinte Co ON Co.IDConferinta == C.IDConferinta WHERE Co.Nume=’%s’ AND E.Instructor=1;<br/>
În loc de %s se află numele Conferinței selectat de utilizator. Ex de valori: Muntenia, Moldova.<br/>
### Interogarea 2
SELECT E.Nume, E.Prenume, E.IDExplorator FROM Exploratori E INNER JOIN Cluburi C ON E.IDClub == C.IDClub INNER JOIN Conferinte Co ON Co.IDConferinta == C.IDConferinta WHERE Co.Nume=’%s’ AND E.Instructor=0;<br/>
În loc de %s se află numele Conferinței selectat de utilizator. Ex de valori: Muntenia, Moldova.<br/>
### Interogarea 3
SELECT E.Nume, E.Prenume, E.IDExplorator FROM Exploratori E INNER JOIN ExploratoriInProiecte EP ON E.IDExplorator == EP.IDExplorator INNER JOIN Proiecte P ON EP.IDProiect == P.IDProiecte WHERE EP.NrOre > nrOre AND P.Nume =’nume’;<br/>
nrOre este un număr introdus de utilizator<br/>
nume este numele Proiectului selectat de utilizator <br/>
### Interogarea 4
SELECT COUNT(E.IDExplorator) AS NrExploratori FROM Exploratori E INNER JOIN ExploratoriInProiecte EP ON E.IDExplorator = EP.IDExplorator INNER JOIN Cluburi C ON E.IDClub = C.IDClub INNER JOIN Proiecte P ON EP.IDProiect = P.IDProiecte WHERE P.Nume = ‘numeProiect’ AND C.Nume = ‘numeClub’;<br/>
numeProiect poate fi orice nume de proiect introdus<br/>
numeClub poate fi orice nume de club introdus. Ex:<br/>
SELECT COUNT(E.IDExplorator) AS NrExploratori FROM Exploratori E INNER JOIN ExploratoriInProiecte EP ON E.IDExplorator = EP.IDExplorator INNER JOIN Cluburi C ON E.IDClub = C.IDClub INNER JOIN Proiecte P ON EP.IDProiect = P.IDProiecte WHERE P.Nume = 'Prietenie' AND C.Nume = 'Moise' ;<br/>
### Interogarea 5
SELECT DISTINCT P.Nume, P.Prenume FROM Exploratori E INNER JOIN Parinti P ON E.IDParinte = P.IDParinte  INNER JOIN ExploratoriInProiecte EP ON E.IDExplorator = EP.IDExplorator INNER JOIN Proiecte Pr ON Pr.IDProiecte = EP.IDProiect <br/>
 WHERE Pr.Nume = ‘ numeProiect ‘<br/>
numeProiect este numele Proiectului ales de utilizator din lista celor disponibile.<br/>
### Interogarea 6
SELECT P.Nume FROM Proiecte P INNER JOIN ExploratoriInProiecte EP ON P.IDProiecte = EP.IDProiect INNER JOIN Exploratori E ON EP.IDExplorator = E.IDExplorator  GROUP BY P.IDProiecte, P.Nume  HAVING SUM(EP.NrOre) > 0  LIMIT 3;<br/>
## Interogări Complexe
### Interogarea 1
SELECT E.Nume, E.Prenume, E.IDExplorator FROM Exploratori E INNER JOIN ExploratoriInProiecte EP ON E.IDExplorator = EP.IDExplorator GROUP BY  E.Nume, E.Prenume, E.IDExplorator HAVING EP.NrOre > (SELECT AVG(sumaOre) FROM (SELECT Sum(EP.NrOre) AS sumaOre FROM Exploratori E INNER JOIN ExploratoriInProiecte EP ON E.IDExplorator = EP.IDExplorator INNER JOIN Cluburi C ON E.IDClub = C.IDClub INNER JOIN Conferinte CO ON C.IDConferinta = CO.IDConferinta WHERE CO.Nume = 'numeConferinta' GROUP BY  E.Nume, E.Prenume, E.IDExplorator));<br/>
„numeConferinta” reprezintă un nume din lista numelor Conferințelor disponibile și este selectat de utilizator.<br/>
### Interogarea 2
SELECT C.Nume FROM Cluburi C INNER JOIN Exploratori E ON E.IDClub = C.IDClub GROUP BY C.IDClub HAVING COUNT(E.IDExplorator) > (SELECT AVG(nrExplo) FROM (SELECT COUNT(IDExplorator) As nrExplo FROM Exploratori E1 INNER JOIN Cluburi C1 ON E1.IDClub = C1.IDClub GROUP BY C1.IDClub)) LIMIT 2;<br/>
### Interogarea 3
SELECT P.Nume, SUM(EP.NrOre) FROM Proiecte P INNER JOIN ExploratoriInProiecte EP ON P.IDProiecte = EP.IDProiect <br/>
WHERE EP.IDExplorator NOT IN (SELECT E1.IDExplorator FROM Exploratori E1 INNER JOIN Cluburi C1 ON E1.IDClub = C1.IDClub<br/>
	INNER JOIN Conferinte Cf1 ON C1.IDConferinta = Cf1.IDConferinta WHERE C1.Nume = 'Muntenia') AND <br/>
	EP.IDExplorator IN (SELECT E2.IDExplorator FROM Exploratori E2 INNER JOIN Cluburi C2 ON E2.IDClub = C2.IDClub<br/>
	INNER JOIN Conferinte Cf2 ON C2.IDConferinta = Cf2.IDConferinta WHERE C2.Nume = 'Moldova' AND C2.IDConducator = E2.IDExplorator)<br/>
GROUP BY P.Nume<br/>
HAVING SUM(EP.NrOre) > 10 <br/>
„numeConferinta” reprezintă un nume din lista numelor Conferințelor disponibile și este selectat de utilizator.<br/>
n este orice număr natural.<br/>
### Interogarea 4
SELECT \* FROM Conferinte Cf INNER JOIN Cluburi C ON Cf.IDConferinta = C.IDConferinta INNER JOIN Exploratori E ON E.IDClub = C.IDClub<br/>
INNER JOIN ExploratoriInProiecte EP<br/>
GROUP BY CF.Nume <br/>
HAVING SUM(EP.NrOre > 1) AND Cf.Nume IN<br/>
(SELECT Cf.Nume FROM Conferinte Cf1 INNER JOIN Cluburi C1 ON Cf1.IDConferinta = C1.IDConferinta INNER JOIN Exploratori E1 ON E1.IDClub = C1.IDClub<br/>
GROUP BY CF1.Nume<br/>
HAVING E1.IDExplorator IN (SELECT E2.IDExplorator FROM Exploratori E2 INNER JOIN ExploratoriInProiecte EP2 ON E2.IDExplorator = EP2.IDExplorator))<br/>


###Bibliography
SQLite Primary Key. (2019). Preluat de pe SQLiteTutorial: https://www.sqlitetutorial.net/sqlite-primary-key/


