-- a
SELECT firstName, lastName 
FROM Students;

-- b 
SELECT firstName, lastName 
FROM Students 
ORDER BY lastName, firstName;

-- c
SELECT * FROM Students 
WHERE substring(pNbr, 1, 2) = 85;

-- d
SELECT firstName, pNbr 
FROM Students 
WHERE mod(substring(pNbr, 10, 1), 2) = 0;

-- e
SELECT COUNT(*) 
FROM Students;

-- f
SELECT * 
FROM Courses 
WHERE substring(courseCode, 1, 3) = "FMA";

-- g
SELECT * 
FROM Courses 
WHERE credits > 7.5;

-- h
SELECT count(*) 
FROM Courses 
GROUP BY level;

-- i
SELECT courseCode 
FROM TakenCourses 
WHERE pNbr = "910101-1234";

-- j
SELECT courseName, credits 
FROM Courses 
NATURAL JOIN TakenCourses 
WHERE pNbr = "910101-1234";

-- k
SELECT sum(credits) 
FROM Courses 
NATURAL JOIN TakenCourses 
WHERE pNbr = "910101-1234";

-- l
SELECT avg(grade) 
FROM TakenCourses 
WHERE pNbr = "910101-1234";

-- m
SELECT courseCode 
FROM TakenCourses 
NATURAL JOIN Students 
WHERE firstName = "Eva" 
  AND lastName = "Alm";

SELECT courseName, credits 
FROM Courses 
NATURAL JOIN TakenCourses 
NATURAL JOIN Students 
WHERE firstName = "Eva" 
  AND lastName = "Alm";

SELECT sum(credits) 
FROM Courses 
NATURAL JOIN TakenCourses 
NATURAL JOIN Students 
WHERE firstName = "Eva" 
  AND lastName = "Alm";

SELECT avg(grade) 
FROM TakenCourses 
NATURAL JOIN Students 
WHERE firstName = "Eva" 
  AND lastName = "Alm";

-- n
SELECT firstName, lastName 
FROM TakenCourses 
RIGHT OUTER JOIN Students 
  ON Students.pNbr = TakenCourses.pNbr 
WHERE courseCode IS NULL;

-- o
SELECT * 
FROM (
  SELECT firstName, lastName, pNbr, avg(grade) AS avg_grade 
  FROM TakenCourses 
  NATURAL JOIN Students 
  GROUP BY pNbr 
  ) foo 
ORDER BY avg_grade DESC
LIMIT 3;

-- p
SELECT Students.pNbr, coalesce(sum(credits), 0)
FROM Courses 
INNER JOIN TakenCourses ON TakenCourses.courseCode = Courses.courseCode 
RIGHT OUTER JOIN Students ON TakenCourses.pNbr = Students.pNbr
GROUP BY Students.pNbr;

-- q
SELECT s1.pNbr, s1.firstName, s1.lastName
FROM Students s1
INNER JOIN (
  SELECT pNbr, firstName, lastName, COUNT(*) 
  FROM Students 
  GROUP BY firstName, lastName 
  HAVING COUNT(*) > 1  
) s2 ON s1.firstName = s2.firstName AND s1.lastName = s2.lastName; 

