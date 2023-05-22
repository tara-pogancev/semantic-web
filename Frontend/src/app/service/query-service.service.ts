import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class QueryService {
  private baseUrl = 'http://localhost:8080/';

  constructor(private _http: HttpClient) {}

  runQuery(query: String) {
    return this._http.post(`${this.baseUrl}chat`, query, {
      responseType: 'text',
    });
  }

  resourcesForCourseSample() {
    var bodyArg = {
      course: 'Semantic Web',
    };
    return this._http.post(
      `${this.baseUrl}sparql/resources-for-course`,
      bodyArg,
      {
        responseType: 'text',
      }
    );
  }

  resourcesForKnowledgeUnitSample() {
    var bodyArg = {
      knowledgeUnit: 'Basic Analysis',
    };
    return this._http.post(
      `${this.baseUrl}sparql/resources-for-knowledge-unit`,
      bodyArg,
      {
        responseType: 'text',
      }
    );
  }

  coursesForAuthorSample() {
    var bodyArg = {
      author: 'Jelena Jelic',
    };
    return this._http.post(
      `${this.baseUrl}sparql/courses-with-resources-by-author`,
      bodyArg,
      {
        responseType: 'text',
      }
    );
  }

  coursesAndAuthorsForLearningOutcomeSample() {
    var bodyArg = {
      learningOutcome:
        'Describe security concerns in designing applications for use over wireless networks',
    };
    return this._http.post(
      `${this.baseUrl}sparql/courses-and-authors-for-learning-outcome`,
      bodyArg,
      {
        responseType: 'text',
      }
    );
  }

  documentsCitedByAuthorInFormatSample() {
    var bodyArg = {
      author: 'Tara Pogancev',
      resourceFormat: 'PDF',
    };
    return this._http.post(
      `${this.baseUrl}sparql/documents-cited-by-author-in-format`,
      bodyArg,
      {
        responseType: 'text',
      }
    );
  }
}
