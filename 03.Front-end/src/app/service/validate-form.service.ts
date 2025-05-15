/*
  Copyright(C) 2025 Luvina Software Company
  validate-form.service.ts 15/5/2025 hoaivd
*/

import { Injectable } from '@angular/core';
import { AbstractControl, ValidatorFn } from '@angular/forms';
import { ERROR_MESSAGES } from '../shared/utils/error-messages.constants';
import { ERROR_CODES } from '../shared/utils/error-code.constants';
import { ERROR_KEYS } from '../shared/utils/error-key.constants';

@Injectable({
  providedIn: 'root'
})

/**
 * Serivce dùng để validate các hạng mục và hiển thị lỗi với từng trường hợp lỗi tương ứng.
 * 
 * @author hoaivd
 */
export class ValidateFormService {
  /**
   * Validator tùy chỉnh cho trường アカウント名 (employeeLoginId).
   * Áp dụng các quy tắc kiểm tra sau:
   * - Chỉ chấp nhận các ký tự a-z, A-Z, 0-9 và dấu gạch dưới (_) → nếu vi phạm trả về lỗi 'invalidChars'
   * - Không được bắt đầu bằng số → nếu vi phạm trả về lỗi 'startsWithNumber'
   * - Không kiểm tra độ dài tại đây (độ dài nên kiểm tra bằng Validators.maxLength riêng biệt)
   * 
   * @returns ValidatorFn - Hàm validator áp dụng cho FormControl
 */
  checkEnglishHalfSize(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const value = control.value;
      if (!value) return null;

      if (!/^[a-zA-Z0-9_]+$/.test(value)) {
        return { invalidChars: true };
      }

      if (/^[0-9]/.test(value)) {
        return { startsWithNumber: true };
      }

      return null;
    };
  }

  checkKanaHalfSize(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const value = control.value;
      if (!value) return null;

      // \uFF66-\uFF9F: Các ký tự Katakana Halfwidth theo chuẩn Unicode (U+FF66 đến U+FF9F)
      // \uFF70	Dấu kéo dài âm halfwidth (ｰ, mã Unicode U+FF70)
      // \s	Khoảng trắng (space, tab, line break...)
      const halfwidthKatakanaRegex = /^[\uFF66-\uFF9F\uFF70\s]$/;

      if (!halfwidthKatakanaRegex.test(value)) {
        return { invalidKanaFormat: true };
      }

      return null;
    }
  }

  /**
   * Trả về thông điệp lỗi phù hợp với lỗi hiện tại của một FormControl.
   * Áp dụng cho các lỗi đã khai báo trong validator, gồm:
   * - 'required' (bắt buộc nhập) → mã lỗi ER001
   * - 'maxlength' (vượt quá độ dài cho phép) → mã lỗi ER006
   * - 'invalidChars' hoặc 'startsWithNumber' (định dạng không hợp lệ) → mã lỗi ER019
   * 
   * @param control - FormControl đang kiểm tra lỗi
   * @param fieldName - Tên hiển thị của trường để đưa vào thông điệp lỗi (ví dụ: アカウント名)
   * @returns Chuỗi thông điệp lỗi tương ứng, hoặc rỗng nếu không có lỗi
 */
  getErrorMessage(control: AbstractControl | null, fieldName: string): string {
    if (!control || !control.errors) return '';
  
    let errorMessage = '';
  
    for (const [errorKey, errorValue] of Object.entries(control.errors)) {
      switch (errorKey) {
        case ERROR_KEYS.REQUIRED:
          errorMessage = ERROR_MESSAGES[ERROR_CODES.REQUIRED_FIELD](fieldName);
          break;
        case ERROR_KEYS.MAX_LENGTH:
          errorMessage = ERROR_MESSAGES[ERROR_CODES.MAX_LENGTH_EXCEEDED](fieldName, errorValue.requiredLength);
          break;
        case ERROR_KEYS.INVALID_CHARS:
        case ERROR_KEYS.STARTS_WITH_NUMBER:
          errorMessage = ERROR_MESSAGES[ERROR_CODES.INVALID_USERNAME_FORMAT](fieldName);
          break;
        case ERROR_KEYS.INVALID_KANA_FORMAT:
          errorMessage = ERROR_MESSAGES[ERROR_CODES.KANA_REQUIRED](fieldName);
          break;
      }
  
      if (errorMessage) break; // Dừng vòng lặp ngay khi có lỗi đầu tiên
    }
  console.log(errorMessage)
    return errorMessage;
  } 

  /**
   * Xác định xem có nên hiển thị thông điệp lỗi cho một FormControl hay không.
   * Điều kiện hiển thị lỗi:
   * - Control tồn tại và không hợp lệ (invalid)
   * - Control đã được người dùng tương tác (dirty hoặc touched)
   * 
   * Mục đích: tránh hiển thị lỗi quá sớm trước khi người dùng nhập hoặc tương tác.
   * 
   * @param control - FormControl cần kiểm tra
   * @returns true nếu nên hiển thị lỗi, ngược lại false
 */
  shouldShowError(control: AbstractControl | null): boolean {
    return !!control && control.invalid && (control.dirty || control.touched);
  }

}
