import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Tpr } from './tpr.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TprService {

    private resourceUrl = SERVER_API_URL + 'api/tprs';

    constructor(private http: Http) { }

    create(tpr: Tpr): Observable<Tpr> {
        const copy = this.convert(tpr);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(tpr: Tpr): Observable<Tpr> {
        const copy = this.convert(tpr);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Tpr> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Tpr.
     */
    private convertItemFromServer(json: any): Tpr {
        const entity: Tpr = Object.assign(new Tpr(), json);
        return entity;
    }

    /**
     * Convert a Tpr to a JSON which can be sent to the server.
     */
    private convert(tpr: Tpr): Tpr {
        const copy: Tpr = Object.assign({}, tpr);
        return copy;
    }
}
