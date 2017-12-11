#version 430 core

in vec2 out_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;

out vec4 fragcolor;

uniform sampler2D textureSampler;
uniform vec3 lightColor;

void main(void)
{
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);

	float nDotl = dot(unitNormal, unitLightVector);
	float brightness = max(nDotl, 0.1);
	vec3 diffuse = brightness * lightColor;

	vec4 textureColor = texture(textureSampler, out_textureCoords);

	fragcolor = vec4(diffuse, 1.0) * textureColor;
}
