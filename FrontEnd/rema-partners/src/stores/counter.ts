import { defineStore } from 'pinia';

interface Translations {
  [key: string]: {
    [key: string]: string;
  };
}

export const useutf8Store = defineStore('utf8', {
  state: () => ({
    currentLanguage: 'es',
    translations: {
      en: {
        'login.title': 'Sign in to your account',
        'login.email': 'Email address',
        'login.password': 'Password',
        'login.remember': 'Remember me',
        'login.forgot': 'Forgot your password?',
        'login.submit': 'Sign in',
        'language': 'Language',
        'language.en': 'English',
        'language.es': 'Spanish',
        'language.cat': 'Catalan',
        'language.select': 'Select your language',
        'login.login': 'Login',
        'login.register': 'Register',
        'login.forgotPassword': 'Forgot Password',
        'signup.title': 'Create an account',
        'signup.email': 'Email address',
        'signup.password': 'Password',
        'signup.confirmPassword': 'Confirm Password',
        'signup.submit': 'Create Account',
        'signup.login': 'Already have an account? Sign in',
        'signup.passwordMismatch': 'Passwords do not match',
        'signup.passwordLength': 'Password must be at least 6 characters long',
        'signup.invalidEmail': 'Invalid email address',
        'signup.emptyFields': 'All fields are required',
        'modal.close': 'Close',
        'modal.error': 'Error',
        'modal.success': 'Success',
        'modal.warning': 'Warning',
        'modal.info': 'Information',
        'modal.confirm': 'Confirm',
        'modal.cancel': 'Cancel',
        'modal.save': 'Save',
        'modal.delete': 'Delete',
        'modal.edit': 'Edit'
      },
      es: {
        'login.title': 'Iniciar sesión',
        'login.email': 'Correo electrónico',
        'login.password': 'Contraseña',
        'login.remember': 'Recordarme',
        'login.forgot': '¿Olvidaste tu contraseña?',
        'login.submit': 'Entrar',
        'language': 'Idioma',
        'language.en': 'Inglés',
        'language.es': 'Español',
        'language.cat': 'Catalán',
        'language.select': 'Selecciona tu idioma',
        'login.login': 'Iniciar sesión',
        'login.register': 'Registrar',
        'login.forgotPassword': 'Olvidé mi contraseña',
        'signup.title': 'Crear una cuenta',
        'signup.email': 'Correo electrónico',
        'signup.password': 'Contraseña',
        'signup.confirmPassword': 'Confirmar contraseña',
        'signup.submit': 'Crear cuenta',
        'signup.login': '¿Ya tienes una cuenta? Inicia sesión',
        'signup.passwordMismatch': 'Las contraseñas no coinciden',
        'signup.passwordLength': 'La contraseña debe tener al menos 6 caracteres',
        'signup.invalidEmail': 'Correo electrónico no válido',
        'signup.emptyFields': 'Todos los campos son obligatorios',
        'modal.close': 'Cerrar',
        'modal.error': 'Error',
        'modal.success': 'Éxito',
        'modal.warning': 'Advertencia',
        'modal.info': 'Información',
        'modal.confirm': 'Confirmar',
        'modal.cancel': 'Cancelar',
        'modal.save': 'Guardar',
        'modal.delete': 'Eliminar',
        'modal.edit': 'Editar'
      },
      cat: {
        'login.title': 'Iniciar sessió',
        'login.email': 'Correu electrònic',
        'login.password': 'Contrasenya',
        'login.remember': 'Recorda\'m',
        'login.forgot': 'Has oblidat la teva contrasenya?',
        'login.submit': 'Entrar',
        'language': 'Idioma',
        'language.en': 'Anglès',
        'language.es': 'Espanyol',
        'language.cat': 'Català',
        'language.select': 'Selecciona el teu idioma',
        'login.login': 'Iniciar sessió',
        'login.register': 'Registrar',
        'login.forgotPassword': 'He oblidat la meva contrasenya',
        'signup.title': 'Crear un compte',
        'signup.email': 'Correu electrònic',
        'signup.password': 'Contrasenya',
        'signup.confirmPassword': 'Confirmar contrasenya',
        'signup.submit': 'Crear compte',
        'signup.login': 'Ja tens un compte? Inicia sessió',
        'signup.passwordMismatch': 'Les contrasenyes no coincideixen',
        'signup.passwordLength': 'La contrasenya ha de tenir almenys 6 caràcters',
        'signup.invalidEmail': 'Correu electrònic no vàlid',
        'signup.emptyFields': 'Tots els camps són obligatoris',
        'modal.close': 'Tancar',
        'modal.error': 'Error',
        'modal.success': 'Èxit',
        'modal.warning': 'Advertència',
        'modal.info': 'Informació',
        'modal.confirm': 'Confirmar',
        'modal.cancel': 'Cancel·lar',
        'modal.save': 'Desar',
        'modal.delete': 'Eliminar',
        'modal.edit': 'Editar'
      },
    } as Translations
  }),
  actions: {
    setLanguage(lang: string) {
      this.currentLanguage = lang;
    },
    t(key: string): string {
      return this.translations[this.currentLanguage][key] || key;
    }
  }
});
