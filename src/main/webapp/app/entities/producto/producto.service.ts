import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Producto } from './producto.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ProductoService {

    private resourceUrl = SERVER_API_URL + 'api/productos';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(producto: Producto): Observable<Producto> {
        const copy = this.convert(producto);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(producto: Producto): Observable<Producto> {
        const copy = this.convert(producto);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Producto> {
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
     * Convert a returned JSON object to Producto.
     */
    private convertItemFromServer(json: any): Producto {
        const entity: Producto = Object.assign(new Producto(), json);
        entity.vencimiento = this.dateUtils
            .convertLocalDateFromServer(json.vencimiento);
        return entity;
    }

    /**
     * Convert a Producto to a JSON which can be sent to the server.
     */
    private convert(producto: Producto): Producto {
        const copy: Producto = Object.assign({}, producto);
        copy.vencimiento = this.dateUtils
            .convertLocalDateToServer(producto.vencimiento);
        return copy;
    }
}
