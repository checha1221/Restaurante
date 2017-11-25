import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Platillo } from './platillo.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PlatilloService {

    private resourceUrl = SERVER_API_URL + 'api/platillos';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(platillo: Platillo): Observable<Platillo> {
        const copy = this.convert(platillo);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(platillo: Platillo): Observable<Platillo> {
        const copy = this.convert(platillo);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Platillo> {
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
     * Convert a returned JSON object to Platillo.
     */
    private convertItemFromServer(json: any): Platillo {
        const entity: Platillo = Object.assign(new Platillo(), json);
        entity.vencimiento = this.dateUtils
            .convertLocalDateFromServer(json.vencimiento);
        return entity;
    }

    /**
     * Convert a Platillo to a JSON which can be sent to the server.
     */
    private convert(platillo: Platillo): Platillo {
        const copy: Platillo = Object.assign({}, platillo);
        copy.vencimiento = this.dateUtils
            .convertLocalDateToServer(platillo.vencimiento);
        return copy;
    }
}
