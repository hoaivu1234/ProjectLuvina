/**
 * Copyright(C) 2025  Luvina Software Company
 * shorten-text.ts, 11/05/2025 hoaivd
 */

import {Pipe, PipeTransform} from '@angular/core';

@Pipe({name: 'shortenText'})

/**
 * Pipe dùng để rút gọn chuỗi văn bản nếu vượt quá độ dài cho phép.
 * 
 * @author hoaivd
 */
export class ShortenTextPipe implements PipeTransform {

  /**
   * Hàm transform rút gọn chuỗi nếu vượt quá độ dài tối đa.
   *
   * @param text Chuỗi văn bản đầu vào cần rút gọn
   * @param maxLength Độ dài tối đa cho phép của chuỗi (mặc định là 22 ký tự)
   * @returns Chuỗi đã được rút gọn kèm dấu "..." nếu cần, hoặc giữ nguyên nếu không vượt quá độ dài
   */
  transform(text: string | undefined, maxLength: number = 22): string {
    if (text && text.length > maxLength) {
      return text.substring(0, maxLength) + '...';
    }
    return text ?? '';
  }
}

