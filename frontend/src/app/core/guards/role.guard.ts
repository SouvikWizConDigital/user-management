import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const roleGuard = (expectedRole: string): CanActivateFn => {
  return () => {
    const router = inject(Router);
    const userRoles = JSON.parse(localStorage.getItem('roles') || '[]'); 

    if (userRoles.includes(expectedRole)) {
      return true;
    }

    router.navigate(['/unauthorized']);
    return false;
  };
};