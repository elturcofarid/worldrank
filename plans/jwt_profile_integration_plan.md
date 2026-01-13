# JWT Profile Integration Plan

## Current Architecture Analysis

The WorldRank application is a Spring Boot-based social platform for location-based photo sharing and scoring. Key components:

- **Security**: JWT-based authentication with custom JwtFilter and SecurityConfig allowing permitAll access
- **Entities**: User, Profile, Publicacion, Lugar, Visita with JPA/Hibernate spatial support
- **Services**: AuthService (registration only), PublicacionService, etc.
- **Controllers**: AuthController (login stub), PublicacionController

## Current Issues

1. JWT tokens only contain user ID as subject
2. No profile data included in tokens
3. PublicacionController cannot access user profile information
4. Authentication flow incomplete (login not implemented)

## Proposed Changes

### 1. Enhance JWT Token Generation
- Modify `JwtProvider.generateToken()` to accept user ID and profile data
- Include profile information as JWT claims (e.g., username, score, etc.)

### 2. Implement Complete Authentication Flow
- Update `AuthService.login()` to:
  - Validate user credentials
  - Load user profile
  - Generate JWT with profile claims
- Update `AuthController.login()` to return actual JWT token

### 3. Reconfigure Security
- Re-enable `oauth2ResourceServer` in `SecurityConfig` for JWT processing
- Remove `AuthenticationManager` bean to prevent recursion
- Maintain `permitAll` for public access while allowing JWT processing

### 4. Update Controllers
- Modify `PublicacionController.crear()` to use `@AuthenticationPrincipal Jwt jwt`
- Extract user ID from `jwt.getSubject()`
- Access profile data from `jwt.getClaims()` if needed

### 5. JWT Claims Structure
```json
{
  "user": "user-uuid",
  "profile": "profile-uuid",
  "iat": 1640995200,
  "exp": 1641081600
}
```

## Implementation Steps

1. **JwtProvider Enhancement**
   - Add `generateToken(String subject, Profile profile)` method
   - Include profile data in claims

2. **AuthService Updates**
   - Add `JwtProvider` dependency
   - Implement `login(String email, String password)` method
   - Return JWT token with profile data

3. **SecurityConfig Adjustments**
   - Add `oauth2ResourceServer` configuration
   - Remove `AuthenticationManager` bean

4. **Controller Modifications**
   - Use `@AuthenticationPrincipal Jwt jwt` in `PublicacionController`
   - Extract user data from JWT claims

5. **Testing**
   - Verify login returns JWT with profile
   - Confirm publication creation uses JWT data
   - Ensure anonymous access still works if needed

## Benefits

- Rich JWT tokens containing user profile information
- Secure access to user data in controllers
- Maintainable authentication flow
- Better separation of concerns

## Risks

- Potential recursion if SecurityConfig not properly configured
- Increased token size with profile data
- Need to handle token refresh for profile updates