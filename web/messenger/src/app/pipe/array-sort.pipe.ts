import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
    name: 'ascSort'
})
export class ArraySortPipeAsc implements PipeTransform {
    transform(array: any, field: string, asc: boolean): any[] {
        if (!Array.isArray(array)) {
            return;
        }

        array.sort((a: any, b: any) => {
            if (a[field] < b[field]) {
                return -1;
            } else if (a[field] > b[field]) {
                return 1;
            } else {
                return 0;
            }
        });
        return array;
    }
}

@Pipe({
    name: 'descSort'
})
export class ArraySortPipeDesc implements PipeTransform {
    transform(array: any, field: string, asc: boolean): any[] {
        if (!Array.isArray(array)) {
            return;
        }

        array.sort((a: any, b: any) => {
            if (a[field] < b[field]) {
                return 1;
            } else if (a[field] > b[field]) {
                return -1;
            } else {
                return 0;
            }
        });
        return array;
    }
}
