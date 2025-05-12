import { Certification } from "./certification.model";

export interface CertificationResponse {
    code: number;
    certifications: Certification[];
  }