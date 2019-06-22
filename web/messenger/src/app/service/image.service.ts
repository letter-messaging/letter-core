import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {API_URL} from '../../../globals';
import {NewImage} from '../dto/NewImage';
import {ImageCompressionMode} from '../dto/enum/ImageCompressionMode';

@Injectable({
	providedIn: 'root'
})
export class ImageService {

	constructor(private http: HttpClient) {
	}

	upload(token: string, file): Observable<NewImage> {
		const formData = new FormData();
		formData.append('image', file);

		return this.http.post<NewImage>(API_URL + 'image/upload', formData, {
			headers: {
				'Auth-Token': token
			}
		});
	}

	delete(token: string, imageId: number): Observable<void> {
		return this.http.get<void>(API_URL + 'image/delete', {
			headers: {
				'Auth-Token': token
			},
			params: {
				'imageId': imageId.toString()
			}
		});
	}

	static getImagePathByCompressionMode(path: string, compressionMode: ImageCompressionMode): string {
		if (compressionMode === ImageCompressionMode.FULL) return path;

		const pathName = path.substring(0, path.lastIndexOf('.'));
		const extension = path.substring(path.lastIndexOf('.'));
		return `${pathName}_${compressionMode.toString()}${extension}`;
	}

}
