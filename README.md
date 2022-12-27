# Semantic Web Project
### ACM Curriculum Ontology

### Generating RDF file
**1. Generate starter RDF**
`http://localhost:8080/data/generate`

**2. Upload File Metadata**
```
http://localhost:8080/data/upload

{
	"format":  "PDF",
	"name":  "Introduction to Mathematical Analysis",
	"author":  "Tara Pogancev",
	"difficultyLevel":  1,
	"cites":  [{
	"content":  "Analysis 1",
	"section":  5,
	"number":  3},
	{
	"content":  "Algebra Basics",
	"section":  15,
	"number":  2}
	],
	"obtainedBy":  [
		"AR-4-1",
		"AR-4-2"
	],
	"teachesCourses":[
		"C-1",
		"C-3"
	]
}
```

### Implemented SPARQL Queries
**1.	Which resources are the best for learning a specific Course?**
```
http://localhost:8080/sparql/resources-for-course

{
	"course":  "Semantic Web"
}

```

**2. Which resources are the best for learning a specific Knowledge Unit?**
```
http://localhost:8080/sparql/resources-for-knowledge-unit

{
	"knowledgeUnit":  "Basic Analysis"
}
```

**3. For which courses do we need resources by a specific Author?**
```
...
```

**4. Which courses should i enroll in order to get a specific Learning Outcome? Which authors are the creators for those courses' Learning Resources?**
```
...
```

**5. Which documents are cited by a specific Author in a specific Resource Format?**
```
...
```