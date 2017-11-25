import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Reserva } from './reserva.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ReservaService {

    private resourceUrl = SERVER_API_URL + 'api/reservas';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(reserva: Reserva): Observable<Reserva> {
        const copy = this.convert(reserva);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(reserva: Reserva): Observable<Reserva> {
        const copy = this.convert(reserva);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Reserva> {
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
     * Convert a returned JSON object to Reserva.
     */
    private convertItemFromServer(json: any): Reserva {
        const entity: Reserva = Object.assign(new Reserva(), json);
        entity.fecha = this.dateUtils
            .convertLocalDateFromServer(json.fecha);
        return entity;
    }

    /**
     * Convert a Reserva to a JSON which can be sent to the server.
     */
    private convert(reserva: Reserva): Reserva {
        const copy: Reserva = Object.assign({}, reserva);
        copy.fecha = this.dateUtils
            .convertLocalDateToServer(reserva.fecha);
        return copy;
    }
}
