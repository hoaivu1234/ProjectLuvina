import {Pipe, PipeTransform} from '@angular/core';

@Pipe({name: 'shortenText'})

export class ShortenTextPipe implements PipeTransform {
  transform(text: string | undefined, maxLength: number = 22): string {
      if (text && text.length > maxLength) {
        return text.substring(0, maxLength) + '...';
      }
      return text ?? '';
    }
}
