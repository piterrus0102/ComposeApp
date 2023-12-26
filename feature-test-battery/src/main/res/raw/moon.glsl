precision highp float;

uniform vec2 iResolution;
uniform float u_time;

float tanh(float h) {
    return (exp(2.0 * h) - 1.0) / (exp(2.0 * h) + 1.0);
}

/*****************************************************/

float gravity = 1.0;
float waterTension = 0.01;
//constant vec3 skyCol1 = vec3(0.2, 0.4, 0.6);
//constant vec3 skyCol2 = vec3(0.4, 0.7, 1.0);
//constant vec3 skyCol1 = vec3(1.0, 1.0, 0.69);
//constant vec3 skyCol2 = vec3(0.11, 0.33, 0.59);

vec3 skyCol2 = vec3(0.15, 0.3, 0.6);
vec3 skyCol1 = vec3(0.7, 0.8, 1.0);

vec3 sunCol  =  vec3(8.0, 7.0, 6.0)/8.0;
//constant vec3 seaCol1 = vec3(0.0, 0.95, 0.8);//vec3(0.1,0.1,0.1);//vec3(0.1,0.2,0.2);
//constant vec3 seaCol2 = vec3(0.0, 0.8, 0.95);//vec3(10.1,10.1,10.6);//vec3(0.8,0.9,0.6);
vec3 seaCol1 = vec3(0.1,0.1,0.1);//vec3(0.1,0.2,0.2);
vec3 seaCol2 = vec3(10.1,10.1,10.6);//vec3(0.8,0.9,0.6);

float fSunSpeed = 0.10;

vec3 vNightColor   = vec3(.0, 0.0, 0.3);//vec3(.15, 0.3, 0.6);
vec3 vHorizonColor = vec3(0.6, 0.3, 0.4);
vec3 vDayColor     = vec3(0.7,0.8,1);

vec3 vSunColor     = vec3(1.0,0.8,0.6);
vec3 vSunRimColor  = vec3(1.0,0.66,0.33);

vec3 updateSun(float iTime)
{
    float fSpeed = fSunSpeed * iTime;
    return normalize( vec3(cos(fSpeed), sin(fSpeed), 0.0) );
}

vec3 sunDirection(float iTime)
{
    float x_min = 0.21;
    float x_max = -0.9;
    float z_min = 0.8;
    float z_max = 1.4;
    float x = -0.5 - cos(iTime/10.0);
    return normalize(vec3(x, 0.15, z_min));
}

vec3 renderSky(vec3 rd, float iTime)
{
    vec3 v3sunDir = updateSun(iTime);

    float fSunHeight = v3sunDir.y;

    // below this height will be full night color
    float fNightHeight = -0.8;
    // above this height will be full day color
    float fDayHeight   = 0.3;

    float fHorizonLength = fDayHeight - fNightHeight;
    float fHalfHorizonLength = fHorizonLength / 2.0;
    float fInverseHHL = 1.0 / fHalfHorizonLength;
    float fMidPoint = fNightHeight + fHalfHorizonLength;

    float fNightContrib = clamp((fSunHeight - fMidPoint) * (-fInverseHHL), 0.0, 1.0);
    float fHorizonContrib = -clamp(abs((fSunHeight - fMidPoint) * (-fInverseHHL)), 0.0, 1.0) + 1.0;
    float fDayContrib = clamp((fSunHeight - fMidPoint) * ( fInverseHHL), 0.0, 1.0);

    // sky color
    vec3 vSkyColor = vec3(0.0);
    vSkyColor += mix(vec3(0.0),   vNightColor, fNightContrib);   // Night
    vSkyColor += mix(vec3(0.0), vHorizonColor, fHorizonContrib); // Horizon
    vSkyColor += mix(vec3(0.0),     vDayColor, fDayContrib);     // Day

    vec3 col = vSkyColor;

    // atmosphere brighter near horizon
    col -= clamp(rd.y, 0.0, 0.5);

    return col;
}

float gravityWave(vec2 p, float k, float h, float iTime)
{
    float w = sqrt(gravity*k*tanh(k*h));
    return sin(p.y*k + w*iTime);
}

float capillaryWave(vec2 p, float k, float h, float iTime)
{
    float w = sqrt((gravity*k + waterTension*k*k*k)*tanh(k*h));
    return sin(p.y*k + w*iTime);
}

vec2 rot(vec2 p, float a)
{
    float c = cos(a);
    float s = sin(a);
    return vec2(p.x*c + p.y*s, -p.x*s + p.y*c);
}

float seaHeight(vec2 p, float iTime)
{
    float height = 0.0;

    float k = 1.0;
    float kk = 1.3;
    float a = 0.25;
    float aa = 1.0/(kk*kk);

    float h = 0.5;
    p *= vec2(0.5);

    for (int i = 0; i < 3; ++i)
    {
        height += a*gravityWave(p + vec2(i), k, h, iTime);
        p = rot(p, float(i));
        k *= kk;
        a *= aa;
    }

    for (int i = 3; i < 7; ++i)
    {
        height += a*capillaryWave(p + vec2(i), k, h, iTime);
        rot(p, float(i));
        k *= kk;
        a *= aa;
    }

    return height;
}

vec3 seaNormal(vec2 p, float d, float iTime)
{
    vec2 eps = vec2(0.001*pow(d, 1.5), 0.0);
    vec3 n = vec3(
    seaHeight(p + vec2(eps), iTime) - seaHeight(p - vec2(eps), iTime),
    2.0*eps.x,
    seaHeight(p + eps.yx, iTime) - seaHeight(p - eps.yx, iTime)
    );

    return normalize(n);
}

vec3 skyColor(vec3 rd, float iTime)
{
    vec3 sunDir = sunDirection(iTime);
    float sunDot = max(dot(rd, sunDir), 0.0);
    vec3 final = vec3(0.0);
    vec3 sunCol2 = vec3(8.0, 7.0, 6.0)/8.0;

    vec3 sky1Col = skyCol1;
    vec3 sky2Col = skyCol2;//*sin(iTime/5.0)*cos(iTime/5.0)*20.0;

    //vec3 sky1Col = vec3(0.0, 0.0, sin(iTime/10.0));
    //vec3 sky2Col = vec3(0.0, 0.0, cos(iTime/10.0));

    vec3 skyCol11 = sky1Col*sin(iTime/10.0);
    vec3 skyCol22 = sky2Col*sin(iTime/10.0);

    //final += mix(skyCol11, skyCol22, rd.y);
    final += 0.25*sunCol2*pow(sunDot, 200.0);
    final += 2.0*sunCol2*pow(sunDot, 600.0);
    final += renderSky(rd, iTime/1.0);

    return final;
}

vec4 generateWaveScene(vec2 fragCoord, float iTime)
{
    vec2 q=fragCoord.xy/iResolution.xy;
    vec2 p = -1.0 + 2.0*q;

    p.x *= iResolution.x/iResolution.y;

    vec3 ro = vec3(0.0, 10.0, 0.0);
    vec3 ww = normalize(vec3(0.0, -0.1, 1.0));//camera translate (x - left/right, y - up/down)
    vec3 uu = normalize(cross(vec3(0.0,1.0,0.0), ww));//camera rotate
    vec3 vv = normalize(cross(ww,uu));
    vec3 rd = normalize(p.x*uu + p.y*vv + 2.5*ww); //scale

    vec3 col = vec3(0.0);

    float dsea = (0.0 - ro.y)/rd.y;

    vec3 sunDir = sunDirection(iTime);

    vec3 sky = skyColor(rd, iTime);

    if (dsea > 0.0)
    {
        vec3 p = ro + dsea*rd;
        float h = seaHeight(p.xz, iTime);
        vec3 nor = mix(seaNormal(p.xz, dsea, iTime), vec3(0.0, 1.0, 0.0), smoothstep(0.0, 200.0, dsea));
        float fre = clamp(1.0 - dot(-nor,rd), 0.0, 1.0);
        fre = pow(fre, 3.0);
        float dif = mix(0.25, 1.0, max(dot(nor,sunDir), 0.0));

        vec3 seaCol11 = seaCol1;//*sin(iTime/10.0);
        vec3 seaCol22 = seaCol2;//*sin(iTime/10.0);

        vec3 refl = skyColor(reflect(rd, nor), iTime);
        vec3 refr = seaCol11 + dif*seaCol22*0.1;

        col = mix(refr, 0.9*refl, fre);

        float sdea_dot = dsea;//dot(vec3(dsea, dsea, dsea), vec3(dsea, dsea, dsea));
        float atten = max(1.0 - sdea_dot * 0.1, 0.0);
        col += seaCol22 * (p.y - h) * 2.0 * atten;

        col = mix(col, sky, 1.0 - exp(-0.01*dsea));
    }
    else
    {
        col = sky;
    }

    return vec4(col,1.0);
}


void main() {
    gl_FragColor = generateWaveScene(gl_FragCoord.xy, u_time * 4.0);
}
