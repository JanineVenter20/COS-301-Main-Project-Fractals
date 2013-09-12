#include "jni.h"
#include <stdio.h>
#include <stdlib.h>
#include "mimetex.h"

JNIEXPORT jintArray JNICALL Java_fractals_texchat_Mimetex_mime(JNIEnv *env, jobject this, jstring input)
{
	subraster *subr;
	char* chars = (*env)->GetStringUTFChars(env, input, 0);
	subr = rasterize(chars,4);
	if(subr == NULL)
		subr = rasterize("\\rm {Error}",1);
	raster *rast = rastmag(subr->image, 1);

	jintArray arrayA = (*env)->NewIntArray(env , rast->width*rast->height+2);
	jint* array =  (*env)->GetIntArrayElements(env, arrayA, NULL);

	int i = 0;
	int k = 0;
	int m = 0;
	int c = 0;
	array[0] = rast->width;
	array[1] = rast->height;
	for(i =0; i < rast->height; i++) {
		for(k =0; k < rast->width; k++) {
			array[i*rast->width + k+2] = 255-255*((rast->pixmap[m/8] >> m%8) & 1);
			m++;
		}
	}
	//unsigned short int* ret = rast->image->pixmap;

	return arrayA;
 }

