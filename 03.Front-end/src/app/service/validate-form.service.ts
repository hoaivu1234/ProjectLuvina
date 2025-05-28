/*
  Copyright(C) 2025 Luvina Software Company
  validate-form.service.ts 15/5/2025 hoaivd
*/

import { Injectable } from '@angular/core';
import { AbstractControl, ValidatorFn } from '@angular/forms';
import { ERROR_MESSAGES } from '../shared/utils/error-messages.constants';
import { ERROR_CODES } from '../shared/utils/error-code.constants';
import { ERROR_KEYS } from '../shared/utils/error-key.constants';
import { REGEX } from '../shared/utils/regex.constants';

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
  checkValidateLoginId(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const value = control.value;
      if (!value) return null;

      // Chỉ chứa a-z, A-Z, 0-9 và _
      // Định dạng theo đúng thứ tự regex. có nghĩ là nếu để thành 0-9a-zA-Z thì có thể để số ở đầu
      if (!REGEX.LOGIN_ID.test(value)) {
        return { invalidChars: true };
      }

      // Không được bắt đầu bằng số
      if (REGEX.STARTS_WITH_NUMBER.test(value)) {
        return { startsWithNumber: true };
      }

      return null;
    };
  }

  /**
   * Validator kiểm tra trường chỉ chứa ký tự Katakana dạng half-width (半角カナ).
   * Áp dụng các quy tắc kiểm tra sau:
   * - Chỉ chấp nhận các ký tự từ Unicode U+FF66 đến U+FF9F (half-width Katakana)
   * - Cho phép thêm ký tự kéo dài âm thanh half-width: 'ｰ' (U+FF70)
   * - Cho phép khoảng trắng (space)
   *
   * Nếu vi phạm → trả về lỗi 'invalidKanaFormat'
   *
   * @returns ValidatorFn - Hàm validator áp dụng cho FormControl
  */
  checkKanaHalfSize(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const value = control.value;
      if (!value) return null;

      // \uFF66-\uFF9F: Các ký tự Katakana Halfwidth theo chuẩn Unicode (U+FF66 đến U+FF9F)
      // \uFF70	Dấu kéo dài âm halfwidth (ｰ, mã Unicode U+FF70)
      // \s	Khoảng trắng (space, tab, line break...)
      if (!REGEX.HALF_WIDTH_KANA.test(value)) {
        return { invalidKanaFormat: true };
      }

      return null;
    }
  }

  /**
   * Validator kiểm tra trường chỉ chứa các ký tự tiếng Anh dạng half-size (ASCII).
   *
   * Áp dụng các quy tắc sau:
   * - Chấp nhận ký tự nằm trong bảng mã ASCII (từ U+0000 đến U+007F)
   * - Nếu chuỗi chứa bất kỳ ký tự nào ngoài khoảng đó (ví dụ: full-size, Katakana, ký tự Unicode mở rộng) → lỗi 'nonAsciiCharacters'
   *
   * @returns ValidatorFn - Hàm validator áp dụng cho FormControl
 */
  checkEnglishHalfSize(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const value = control.value;
      if (!value) return null;

      // Check tất cả ký tự là ASCII (halfsize tiếng Anh)
      if (!REGEX.HALF_WIDTH_ENGLISH.test(value)) {
        return { nonAsciiCharacters: true }; // chứa ký tự fullsize hoặc Katakana
      }

      return null;
    }
  }

  /**
   * Validator kiểm tra chuỗi chỉ chứa chữ số half-size (0–9).
   *
   * Áp dụng các quy tắc sau:
   * - Chấp nhận các chữ số từ 0 đến 9 dạng half-size (U+0030 đến U+0039)
   * - Không cho phép chữ số full-size hoặc ký tự khác → lỗi 'numberNotHalfSize'
   *
   * @returns ValidatorFn - Hàm validator áp dụng cho FormControl
 */
  checkNumberHalfSize(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const value = control.value; // Lấy giá trị từ control
      if (!value) return null; // Nếu không có giá trị thì không kiểm tra, không báo lỗi

      if (!REGEX.HALF_WIDTH_NUMBER.test(value)) { // Kiểm tra xem giá trị có khớp với regex half-width number không
        return { numberNotHalfSize: true };  // Nếu không khớp -> báo lỗi
      }

      return null; // Nếu hợp lệ -> không có lỗi
    }
  }

  /**
   * Validator kiểm tra định dạng email theo chuẩn cơ bản.
   *
   * Áp dụng các quy tắc sau:
   * - Phần trước dấu @: không được chứa khoảng trắng hoặc ký tự @
   * - Phần sau dấu @ và trước dấu chấm (.): không được chứa khoảng trắng hoặc @
   * - Phải có ít nhất một dấu chấm (.) sau @
   * - Phần sau dấu chấm không được chứa khoảng trắng hoặc @
   *
   * Lưu ý: Đây là kiểm tra đơn giản, chưa đầy đủ theo chuẩn RFC 5322 nhưng đáp ứng hầu hết các trường hợp thực tế.
   *
   * @returns ValidatorFn - Hàm validator áp dụng cho FormControl
 */
  checkValidateEmail(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const value = control.value; // Lấy giá trị người dùng nhập
      if (!value) return null; // Nếu không có giá trị thì không kiểm tra, không báo lỗi

      if (!REGEX.EMAIL.test(value)) { // Kiểm tra giá trị có khớp với định dạng email trong REGEX không
        return { invalidEmailFormat: true }; // Nếu sai định dạng -> trả về lỗi
      }

      return null;  // Nếu đúng định dạng -> không có lỗi
    };
  }

  /**
   * Validator kiểm tra độ dài chuỗi nằm trong khoảng từ minLength đến maxLength (bao gồm cả hai).
   *
   * Áp dụng trong các trường hợp kiểm tra mật khẩu, tên đăng nhập, mã xác thực, v.v.
   *
   * @param minLength - Độ dài tối thiểu cho phép
   * @param maxLength - Độ dài tối đa cho phép
   * @returns ValidatorFn - Hàm validator áp dụng cho FormControl
 */
  checkLengthRangePassword(minLength: number, maxLength: number): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => { // Trả về một hàm validator áp dụng cho AbstractControl (form control)
      const value = control.value;  // Lấy giá trị người dùng nhập từ control
      if (!value) return null; // Nếu không có giá trị (null, undefined, rỗng) thì không kiểm tra, không báo lỗi

      const lenghtValue = value.length; // Tính độ dài của chuỗi
      if (lenghtValue < minLength || maxLength < lenghtValue) { // Nếu độ dài nhỏ hơn minLength hoặc lớn hơn maxLength -> báo lỗi
        return { invalidLengthRange: true }
      }

      return null; // Nếu hợp lệ -> không có lỗi
    };
  }

  /**
   * Validator dùng để kiểm tra 2 trường mật khẩu (`employeeLoginPassword`) và
   * xác nhận mật khẩu (`employeeReLoginPassword`) có giống nhau hay không.
   *
   * - Nếu một trong hai trường chưa có giá trị, validator trả về null (không lỗi).
   * - Nếu cả hai trường có giá trị và giống nhau → hợp lệ (null).
   * - Nếu khác nhau → trả về lỗi { passwordNotMatch: true }
   *
   * @returns ValidatorFn - Áp dụng cho một FormGroup chứa 2 field trên.
 */
  checkPasswordMatch(mode: string): ValidatorFn {
    return (group: AbstractControl): { [key: string]: any } | null => {
      const password = group.get('employeeLoginPassword')?.value;  // Lấy giá trị của trường mật khẩu từ form group
      const confirmPassword = group.get('employeeReLoginPassword')?.value; // Lấy giá trị của trường xác nhận mật khẩu từ form group

      if (mode === 'add') { // Nếu đang ở chế độ 'add'
        if (!password || !confirmPassword) return { passwordNotMatch: true }; // Nếu 1 trong 2 trường bị thiếu -> báo lỗi không khớp mật khẩu
        return password === confirmPassword ? null : { passwordNotMatch: true }; // Nếu mật khẩu và xác nhận khớp nhau -> không có lỗi, ngược lại -> lỗi
      }

      // mode === 'update'
      if (!password && !confirmPassword) {  // Trường hợp cả hai trường đều trống (người dùng không thay đổi mật khẩu) thì không lỗi
        return null; // không nhập gì thì không lỗi
      }

      if (!password && confirmPassword) { // Nếu chỉ có confirmPassword mà không có password -> lỗi
        return { passwordNotMatch: true };
      }

      if (password && !confirmPassword) { // Nếu chỉ có password mà không có confirmPassword -> lỗi
        return { passwordNotMatch: true };
      }

      return password === confirmPassword ? null : { passwordNotMatch: true }; // Nếu cả hai đều có giá trị, kiểm tra xem chúng có khớp không
    };
  }

  /**
   * Validator dùng để kiểm tra `endDate` phải lớn hơn `startDate`.
   *
   * - Nếu một trong hai trường chưa có giá trị, validator trả về null (không lỗi).
   * - Nếu `endDate` lớn hơn `startDate` → hợp lệ (null).
   * - Nếu `endDate` bằng hoặc nhỏ hơn `startDate` → lỗi { invalidEndDate: true }
   *
   * @returns ValidatorFn - Áp dụng cho một FormGroup chứa 2 field ngày.
 */
  checkLargerThanStartDate(): ValidatorFn {
    return (group: AbstractControl): { [key: string]: any } | null => {
      const startDate = group.get('startDate')?.value;
      const endDate = group.get('endDate')?.value;

      if (!startDate || !endDate) return null;

      return startDate < endDate ? null : { invalidEndDate: true };
    };
  }

  /**
   * Trả về thông điệp lỗi phù hợp với lỗi hiện tại của một FormControl.
   * Áp dụng cho các lỗi đã khai báo trong validator, ví dụ:
   * - 'required' (bắt buộc nhập) → mã lỗi ER001
   * - 'maxlength' (vượt quá độ dài cho phép) → mã lỗi ER006
   * - 'invalidChars' hoặc 'startsWithNumber' (định dạng không hợp lệ) → mã lỗi ER019
   * 
   * @param control - FormControl đang kiểm tra lỗi
   * @param fieldName - Tên hiển thị của trường để đưa vào thông điệp lỗi (ví dụ: アカウント名)
   * @param minLength - Độ dài tối thiểu của control (không bắt buộc)
   * @param maxLength - Độ dài tối đa của control (không bắt buộc)
   * @returns Chuỗi thông điệp lỗi tương ứng, hoặc rỗng nếu không có lỗi
 */
  getErrorMessage(control: AbstractControl | null, fieldName: string, minLength?: number, maxLength?: number): string {
    if (!control || !control.errors) return '';   // Nếu control không tồn tại hoặc không có lỗi, trả về chuỗi rỗng

    let errorMessage = '';   // Khởi tạo biến chứa thông báo lỗi

    for (const [errorKey, errorValue] of Object.entries(control.errors)) {   // Duyệt qua từng cặp key-value trong danh sách lỗi của control
      switch (errorKey) {  // Xử lý từng loại lỗi cụ thể theo errorKey
        case ERROR_KEYS.REQUIRED:   // Trường bắt buộc không được để trống
          errorMessage = ERROR_MESSAGES[ERROR_CODES.REQUIRED_FIELD](fieldName);
          break;
        case ERROR_KEYS.MAX_LENGTH: // Độ dài vượt quá giới hạn tối đa
          errorMessage = ERROR_MESSAGES[ERROR_CODES.MAX_LENGTH_EXCEEDED](fieldName, errorValue.requiredLength);
          break;
        case ERROR_KEYS.INVALID_CHARS:
        case ERROR_KEYS.STARTS_WITH_NUMBER: // Các ký tự không hợp lệ hoặc bắt đầu bằng số (dùng chung thông báo)
          errorMessage = ERROR_MESSAGES[ERROR_CODES.INVALID_USERNAME_FORMAT](fieldName);
          break;
        case ERROR_KEYS.INVALID_KANA_FORMAT: // Trường yêu cầu định dạng chữ Katakana nhưng sai định dạng
          errorMessage = ERROR_MESSAGES[ERROR_CODES.KANA_REQUIRED](fieldName);
          break;
        case ERROR_KEYS.NONASCIICHARACTERS: // Trường chứa ký tự không phải ASCII (yêu cầu ký tự half-width)
          errorMessage = ERROR_MESSAGES[ERROR_CODES.HALF_WIDTH_CHAR_REQUIRED](fieldName);
          break;
        case ERROR_KEYS.INVALID_EMAIL_FORMAT: // Trường email không đúng định dạng
          errorMessage = ERROR_MESSAGES[ERROR_CODES.INVALID_FORMAT](fieldName, REGEX.EMAIL);
          break;
        case ERROR_KEYS.INVALID_LENGTH_RANGE:  // Độ dài không nằm trong khoảng yêu cầu
          errorMessage = ERROR_MESSAGES[ERROR_CODES.LENGTH_RANGE](fieldName, minLength, maxLength);
          break;
        case ERROR_KEYS.PASSWORD_NOT_MATHCH: // Mật khẩu xác nhận không trùng khớp với mật khẩu chính
          errorMessage = ERROR_MESSAGES[ERROR_CODES.PASSWORD_MISMATCH]();
          break;
        case ERROR_KEYS.INVALID_END_DATE: // Ngày kết thúc nhỏ hơn ngày bắt đầu
          errorMessage = ERROR_MESSAGES[ERROR_CODES.DATE_ORDER_INVALID]();
          break;
        case ERROR_KEYS.NUMBERNOTHALFSIZE: // Số nhập vào không phải dạng half-size (half-width)
          errorMessage = ERROR_MESSAGES[ERROR_CODES.HALF_WIDTH_NUMBER_REQUIRED](fieldName);
          break;
      }

      if (errorMessage) break; // Dừng vòng lặp ngay khi có lỗi đầu tiên
    }

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
